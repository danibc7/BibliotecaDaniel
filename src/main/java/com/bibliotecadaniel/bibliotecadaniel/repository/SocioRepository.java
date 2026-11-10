
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SocioRepository {

    private final DataSource dataSource;

    public SocioRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SocioDTO> listarTodos() {
        return buscarPorNombreApellidos("","","todos");
    }

    public List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado) {

        List<SocioDTO> socios = new ArrayList<>();

        String sql = """
            SELECT
                s.id,
                s.nombre,
                s.apellidos,
                s.email,
                s.fecha_nacimiento,
                s.activo,
                TIMESTAMPDIFF(YEAR, s.fecha_nacimiento, CURDATE()) AS edad,
                COUNT(p.id) AS prestamos_abiertos
                    FROM socios s
                    LEFT JOIN prestamos p
                        ON s.id = p.id_socio
                        AND p.fecha_devolucion IS NULL
                        WHERE (LOWER(s.nombre) LIKE LOWER(?)
                            OR LOWER(s.apellidos) LIKE LOWER(?))
                        AND LOWER(s.email) LIKE LOWER(?)
                        AND (? = 'todos'
                            OR (? = 'activos' AND s.activo = 1)
                            OR (? = 'inactivos' AND s.activo = 0))
                        GROUP BY
                s.id,
                s.nombre,
                s.apellidos,
                s.email,
                s.fecha_nacimiento,
                s.activo
            ORDER BY s.id
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {


            String filtroNombre = "%" + (nombreApellidos == null ? "" : nombreApellidos) + "%";

            String filtroEmail = "%" + (email == null ? "" : email) + "%";

            ps.setString(1, filtroNombre);
            ps.setString(2, filtroNombre);
            ps.setString(3, filtroEmail);

            String filtroEstado = (estado == null || estado.isBlank()) ? "todos" : estado;

            ps.setString(4, filtroEstado);
            ps.setString(5, filtroEstado);
            ps.setString(6, filtroEstado);

            try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                SocioDTO socio = new SocioDTO(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getBoolean("activo"),
                        rs.getInt("edad"),
                        rs.getInt("prestamos_abiertos")
                );

                socios.add(socio);
            }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los socios", e);
        }

        return socios;
    }

    public void guardarSocio(NuevoSocioDTO socio) {

        String sql = """
            INSERT INTO socios
            (nombre, apellidos, email, fecha_nacimiento, fecha_alta, activo)
            VALUES (?, ?, ?, ?, CURDATE(), 1)
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, socio.getNombre());
            ps.setString(2, socio.getApellidos());
            ps.setString(3, socio.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(socio.getFechaNacimiento()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el socio", e);
        }
    }

    public boolean existeEmail(String email) {

        String sql = "SELECT COUNT(*) FROM socios WHERE LOWER(email) = LOWER(?)";

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar el email", e);
        }

        return false;
    }
    public List<SocioDTO> listarActivos() {

        List<SocioDTO> socios = new ArrayList<>();

        String sql = """
            SELECT
                s.id,
                s.nombre,
                s.apellidos,
                s.email,
                s.fecha_nacimiento,
                s.fecha_alta,
                s.activo,
                TIMESTAMPDIFF(YEAR, s.fecha_nacimiento, CURDATE()) AS edad,
                (
                    SELECT COUNT(*)
                    FROM prestamos p
                    WHERE p.id_socio = s.id
                    AND p.fecha_devolucion IS NULL
                ) AS prestamos_abiertos
            FROM socios s
            WHERE s.activo = 1
            ORDER BY s.apellidos, s.nombre
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                SocioDTO socio = new SocioDTO(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getBoolean("activo"),
                        rs.getInt("edad"),
                        rs.getInt("prestamos_abiertos")
                );

                socios.add(socio);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al listar los socios activos", e);
        }

        return socios;
    }

}

