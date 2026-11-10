
package com.bibliotecadaniel.bibliotecadaniel.repository;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.time.Period;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;

@Repository
public class PrestamoRepository {

    private final DataSource dataSource;

    public PrestamoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int contarPrestamosAbiertos(int idSocio) {

        String sql = """
                SELECT COUNT(*)
                FROM prestamos
                WHERE id_socio = ?
                AND fecha_devolucion IS NULL
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar los préstamos abiertos", e);
        }
        return 0;
    }

    public boolean tienePrestamosVencidos(int idSocio) {

        String sql = """
            SELECT COUNT(*)
            FROM prestamos
            WHERE id_socio = ?
            AND fecha_devolucion IS NULL
            AND fecha_limite < CURDATE()
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar los préstamos vencidos", e);
        }
        return false;
    }

    public boolean copiaDisponible(int idCopia) {

        String sql = """
            SELECT COUNT(*)
            FROM copias c
            WHERE c.id = ?
            AND c.activa = 1
            AND NOT EXISTS (
                SELECT 1
                FROM prestamos p
                WHERE p.id_copia = c.id
                AND p.fecha_devolucion IS NULL
            )
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar la disponibilidad de la copia", e);
        }
        return false;
    }

    public int[] obtenerDatosVideojuego(int idCopia) {

        String sql = """
            SELECT v.pegi, v.anio_lanzamiento
            FROM copias c
            JOIN videojuegos v ON c.id_videojuego = v.id
            WHERE c.id = ?
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new int[]{
                            rs.getInt("pegi"),
                            rs.getInt("anio_lanzamiento")
                    };
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los datos del videojuego", e);
        }

        throw new IllegalArgumentException("La copia seleccionada no existe");
    }

    public LocalDate obtenerNacimientoSocio(int idSocio) {

        String sql = """
            SELECT fecha_nacimiento
            FROM socios
            WHERE id = ?
            AND activo = 1
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getDate("fecha_nacimiento").toLocalDate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la fecha de nacimiento del socio", e);
        }

        throw new IllegalArgumentException("El socio seleccionado no existe o no está activo");
    }

    public void guardarPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        String sql = """
            INSERT INTO prestamos
            (id_socio, id_copia, fecha_prestamo,fecha_limite, fecha_devolucion, multa)
            VALUES (?, ?, ?, ?, NULL, 0.00)
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            ps.setInt(2, idCopia);
            ps.setDate(3, java.sql.Date.valueOf(fechaPrestamo));
            ps.setDate(4, java.sql.Date.valueOf(fechaLimite));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al guardar el préstamo", e);
        }
    }

    public void bloquearCopia(Connection conexion, int idCopia)
            throws SQLException {
        String sql = """
            SELECT id
            FROM copias
            WHERE id = ?
            FOR UPDATE
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException("La copia seleccionada no existe");
                }
            }
        }
    }
    public void registrarPrestamoSeguro(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        try (Connection conexion = dataSource.getConnection()) {

            conexion.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            conexion.setAutoCommit(false);

            try {
                bloquearSocio(conexion, idSocio);

                bloquearCopia(conexion, idCopia);

                if (contarPrestamosAbiertos(conexion, idSocio) >= 3) {
                    throw new IllegalArgumentException("El socio ya tiene 3 préstamos abiertos");
                }

                if (tienePrestamosVencidos(conexion, idSocio)) {
                    throw new IllegalArgumentException("El socio tiene préstamos vencidos pendientes de devolución");
                }

                if (!copiaDisponible(conexion, idCopia)) {
                    throw new IllegalArgumentException("La copia seleccionada no está disponible");
                }

                comprobarEdadPegi(conexion, idSocio, idCopia);

                insertarPrestamo(conexion, idSocio, idCopia, fechaPrestamo, fechaLimite);

                conexion.commit();

            } catch (Exception e) {
                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el préstamo", e);
        }
    }
    public void bloquearSocio(Connection conexion, int idSocio) throws SQLException {

        String sql = """
            SELECT id
            FROM socios
            WHERE id = ? AND activo = 1
            FOR UPDATE
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException("El socio no existe o está inactivo");
                }
            }
        }
    }

    public int contarPrestamosAbiertos(Connection conexion, int idSocio) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM prestamos
            WHERE id_socio = ?
            AND fecha_devolucion IS NULL
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public boolean tienePrestamosVencidos(Connection conexion, int idSocio) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM prestamos
            WHERE id_socio = ?
            AND fecha_devolucion IS NULL
            AND fecha_limite < CURDATE()
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean copiaDisponible(Connection conexion, int idCopia) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM copias c
            WHERE c.id = ?
            AND c.activa = 1
            AND NOT EXISTS (
                SELECT 1
                FROM prestamos p
                WHERE p.id_copia = c.id
                AND p.fecha_devolucion IS NULL
            )
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public void comprobarEdadPegi(Connection conexion, int idSocio, int idCopia) throws SQLException {

        String sql = """
            SELECT s.fecha_nacimiento, v.pegi
            FROM socios s
            JOIN copias c ON c.id = ?
            JOIN videojuegos v ON c.id_videojuego = v.id
            WHERE s.id = ?
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);
            ps.setInt(2, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException("No se encontraron los datos del socio o videojuego");
                }

                LocalDate fechaNacimiento = rs.getDate("fecha_nacimiento").toLocalDate();

                int pegi = rs.getInt("pegi");

                int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

                if (edad < pegi) {
                    throw new IllegalArgumentException("El socio no tiene la edad mínima para este videojuego");
                }
            }
        }
    }

    public void insertarPrestamo(Connection conexion, int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) throws SQLException {

        String sql = """
            INSERT INTO prestamos
            (id_socio, id_copia, fecha_prestamo,
             fecha_limite, fecha_devolucion, multa)
            VALUES (?, ?, ?, ?, NULL, 0.00)
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            ps.setInt(2, idCopia);
            ps.setDate(3, Date.valueOf(fechaPrestamo));
            ps.setDate(4, Date.valueOf(fechaLimite));

            ps.executeUpdate();
        }
    }

    public ResumenPrestamoDTO obtenerResumenPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        String sql = """
            SELECT
                CONCAT(s.nombre, ' ', s.apellidos) AS socio,
                v.titulo AS videojuego,
                p.nombre AS plataforma,
                c.codigo_inventario
            FROM socios s
            JOIN copias c ON c.id = ?
            JOIN videojuegos v ON c.id_videojuego = v.id
            JOIN plataformas p ON c.id_plataforma = p.id
            WHERE s.id = ?
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCopia);
            ps.setInt(2, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new ResumenPrestamoDTO(
                            rs.getString("socio"),
                            rs.getString("videojuego"),
                            rs.getString("plataforma"),
                            rs.getString("codigo_inventario"),
                            fechaPrestamo,
                            fechaLimite
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el resumen del préstamo", e);
        }

        throw new IllegalArgumentException("No se encontraron los datos para el resumen");
    }

}
