
package com.bibliotecadaniel.bibliotecadaniel.repository;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.time.Period;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoListadoDTO;
import java.util.List;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoAbiertoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenDevolucionDTO;



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

    public List<PrestamoListadoDTO> listarPrestamos(String socio, String videojuego, String estado, LocalDate fechaDesde) {

        List<PrestamoListadoDTO> prestamos = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT pr.id,
                   CONCAT(s.nombre, ' ', s.apellidos) AS socio,
                   v.titulo AS videojuego,
                   pl.nombre AS plataforma,
                   pr.fecha_prestamo,
                   pr.fecha_limite,
                   pr.fecha_devolucion,
                   pr.multa
            FROM prestamos pr
            JOIN socios s ON pr.id_socio = s.id
            JOIN copias c ON pr.id_copia = c.id
            JOIN videojuegos v ON c.id_videojuego = v.id
            JOIN plataformas pl ON c.id_plataforma = pl.id
            WHERE 1 = 1
            """);

        List<Object> parametros = new ArrayList<>();

        if (socio != null && !socio.isBlank()) {
            sql.append(" AND CONCAT(s.nombre, ' ', s.apellidos) LIKE ?");
            parametros.add("%" + socio.trim() + "%");
        }

        if (videojuego != null && !videojuego.isBlank()) {
            sql.append(" AND v.titulo LIKE ?");
            parametros.add("%" + videojuego.trim() + "%");
        }

        if (fechaDesde != null) {
            sql.append(" AND pr.fecha_prestamo >= ?");
            parametros.add(Date.valueOf(fechaDesde));
        }

        sql.append(" ORDER BY pr.fecha_prestamo DESC, pr.id DESC");

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                ps.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet rs = ps.executeQuery()) {

                LocalDate hoy = LocalDate.now();

                while (rs.next()) {

                    LocalDate fechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();

                    LocalDate fechaLimite = rs.getDate("fecha_limite").toLocalDate();

                    Date fechaDevolucionSql = rs.getDate("fecha_devolucion");

                    LocalDate fechaDevolucion = fechaDevolucionSql == null ? null : fechaDevolucionSql.toLocalDate();

                    String estadoCalculado;
                    long diasRetraso = 0;

                    if (fechaDevolucion != null) {
                        estadoCalculado = "DEVUELTO";
                    } else if (fechaLimite.isBefore(hoy)) {
                        estadoCalculado = "VENCIDO";
                        diasRetraso = ChronoUnit.DAYS.between(
                                fechaLimite, hoy);
                    } else {
                        estadoCalculado = "EN_PLAZO";
                    }

                    if (estado != null && !estado.isBlank()
                            && !estado.equals("TODOS")
                            && !estado.equals(estadoCalculado)) {
                        continue;
                    }

                    prestamos.add(new PrestamoListadoDTO(
                            rs.getInt("id"),
                            rs.getString("socio"),
                            rs.getString("videojuego"),
                            rs.getString("plataforma"),
                            fechaPrestamo,
                            fechaLimite,
                            fechaDevolucion,
                            rs.getBigDecimal("multa"),
                            estadoCalculado,
                            diasRetraso
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los préstamos", e);
        }

        return prestamos;
    }

    public List<PrestamoAbiertoDTO> listarPrestamosAbiertos(String socio) {

        List<PrestamoAbiertoDTO> prestamos = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT
                pr.id,
                CONCAT(s.nombre, ' ', s.apellidos) AS socio,
                v.titulo AS videojuego,
                pl.nombre AS plataforma,
                c.codigo_inventario,
                pr.fecha_limite
            FROM prestamos pr
            JOIN socios s ON pr.id_socio = s.id
            JOIN copias c ON pr.id_copia = c.id
            JOIN videojuegos v ON c.id_videojuego = v.id
            JOIN plataformas pl ON c.id_plataforma = pl.id
            WHERE pr.fecha_devolucion IS NULL
            """);

        boolean filtrarSocio = socio != null && !socio.isBlank();

        if (filtrarSocio) {
            sql.append("""
                 AND CONCAT(s.nombre, ' ', s.apellidos) LIKE ?
                """);
        }

        sql.append(" ORDER BY pr.fecha_limite ASC, pr.id ASC");

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            if (filtrarSocio) {
                ps.setString(1, "%" + socio.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {

                LocalDate hoy = LocalDate.now();

                while (rs.next()) {

                    LocalDate fechaLimite = rs.getDate("fecha_limite").toLocalDate();

                    String estado;

                    if (fechaLimite.isBefore(hoy)) {
                        estado = "VENCIDO";
                    } else {
                        estado = "EN_PLAZO";
                    }

                    prestamos.add(new PrestamoAbiertoDTO(
                            rs.getInt("id"),
                            rs.getString("socio"),
                            rs.getString("videojuego"),
                            rs.getString("plataforma"),
                            rs.getString("codigo_inventario"),
                            fechaLimite, estado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los préstamos abiertos", e);
        }

        return prestamos;
    }

    public BigDecimal devolverPrestamo(int idPrestamo, boolean copiaDanada) {

        String sqlBuscar = """
            SELECT id_copia, fecha_limite
            FROM prestamos
            WHERE id = ? AND fecha_devolucion IS NULL
            FOR UPDATE
            """;

        String sqlDevolver = """
            UPDATE prestamos
            SET fecha_devolucion = ?, multa = ?
            WHERE id = ? AND fecha_devolucion IS NULL
            """;

        String sqlDanada = """
            UPDATE copias
            SET estado = 'DAÑADO', activa = 0
            WHERE id = ?
            """;

        try (Connection conexion = dataSource.getConnection()) {

            conexion.setAutoCommit(false);

            try {
                int idCopia;
                LocalDate fechaLimite;
                try (PreparedStatement ps = conexion.prepareStatement(sqlBuscar)) {

                    ps.setInt(1, idPrestamo);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new IllegalStateException("El préstamo no existe o ya fue devuelto.");
                        }

                        idCopia = rs.getInt("id_copia");
                        fechaLimite = rs.getDate("fecha_limite").toLocalDate();
                    }
                }
                LocalDate hoy = LocalDate.now();

                long diasRetraso = Math.max(0, ChronoUnit.DAYS.between(fechaLimite, hoy));

                BigDecimal multa = new BigDecimal("0.50").multiply(BigDecimal.valueOf(diasRetraso));


                try (PreparedStatement ps = conexion.prepareStatement(sqlDevolver)) {

                    ps.setDate(1, java.sql.Date.valueOf(hoy));
                    ps.setBigDecimal(2, multa);
                    ps.setInt(3, idPrestamo);

                    if (ps.executeUpdate() != 1) {
                        throw new IllegalStateException("No se pudo registrar la devolución.");
                    }
                }
                if (copiaDanada) {

                    try (PreparedStatement ps = conexion.prepareStatement(sqlDanada)) {

                        ps.setInt(1, idCopia);

                        if (ps.executeUpdate() != 1) {throw new IllegalStateException("No se pudo actualizar la copia dañada.");
                        }
                    }
                }
                conexion.commit();

                return multa;

            } catch (Exception e) {

                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la devolución.", e);
        }
    }

    public ResumenDevolucionDTO obtenerResumenDevolucion(int idPrestamo) {

        String sql = """
            SELECT
                v.titulo AS videojuego,
                pl.nombre AS plataforma,
                c.codigo_inventario,
                CONCAT(s.nombre, ' ', s.apellidos) AS socio,
                pr.fecha_prestamo,
                pr.fecha_limite,
                pr.fecha_devolucion,
                pr.multa
            FROM prestamos pr
            JOIN socios s ON pr.id_socio = s.id
            JOIN copias c ON pr.id_copia = c.id
            JOIN videojuegos v ON c.id_videojuego = v.id
            JOIN plataformas pl ON c.id_plataforma = pl.id
            WHERE pr.id = ?
              AND pr.fecha_devolucion IS NOT NULL
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalStateException("No se encontró la devolución.");
                }

                LocalDate fechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();

                LocalDate fechaLimite = rs.getDate("fecha_limite").toLocalDate();

                LocalDate fechaDevolucion = rs.getDate("fecha_devolucion").toLocalDate();

                long diasRetraso = Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion));

                return new ResumenDevolucionDTO(
                        rs.getString("videojuego"),
                        rs.getString("plataforma"),
                        rs.getString("codigo_inventario"),
                        rs.getString("socio"),
                        fechaPrestamo, fechaLimite, fechaDevolucion, diasRetraso,
                        rs.getBigDecimal("multa")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el resumen de devolución.", e);
        }
    }

}
