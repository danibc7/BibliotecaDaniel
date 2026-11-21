
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoMasPrestadoDTO;
import java.util.ArrayList;
import java.util.List;
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

    public List<VideojuegoMasPrestadoDTO> listarVideojuegosMasPrestados() {
        List<VideojuegoMasPrestadoDTO> videojuegos = new ArrayList<>();

        String sql = """
                SELECT v.titulo, COUNT(*) AS numero_prestamos
                FROM prestamos p
                JOIN copias c ON p.id_copia = c.id
                JOIN videojuegos v ON c.id_videojuego = v.id
                GROUP BY v.id, v.titulo
                ORDER BY numero_prestamos DESC, v.titulo ASC
                LIMIT 5
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                videojuegos.add(new VideojuegoMasPrestadoDTO(
                        rs.getString("titulo"),
                        rs.getLong("numero_prestamos")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los videojuegos más prestados.", e);
        }

        return videojuegos;
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
