
package com.bibliotecadaniel.bibliotecadaniel.repository;

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

}

