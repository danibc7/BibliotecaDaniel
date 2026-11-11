
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class EstadisticasRepository {

    private final DataSource dataSource;

    public EstadisticasRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public EstadisticasDTO obtenerEstadisticas() {

        String sql = """
                SELECT
                    (SELECT COUNT(*)
                     FROM videojuegos)
                        AS total_videojuegos,

                    (SELECT COUNT(*)
                     FROM socios
                     WHERE activo = 1)
                        AS socios_activos,

                    (SELECT COUNT(*)
                     FROM prestamos
                     WHERE fecha_devolucion IS NULL)
                        AS prestamos_abiertos,

                    (SELECT COUNT(*)
                     FROM prestamos
                     WHERE fecha_devolucion IS NULL
                       AND fecha_limite < CURRENT_DATE())
                        AS prestamos_vencidos
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                return new EstadisticasDTO(
                        rs.getInt("total_videojuegos"),
                        rs.getInt("socios_activos"),
                        rs.getInt("prestamos_abiertos"),
                        rs.getInt("prestamos_vencidos")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las estadísticas de GameVault.", e);
        }

        throw new IllegalStateException("No se pudieron obtener las estadísticas.");
    }
}
