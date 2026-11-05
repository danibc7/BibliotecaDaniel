
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VideojuegoRepository {

    private final DataSource dataSource;

    public VideojuegoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<VideojuegoDTO> listarTodos() {

        List<VideojuegoDTO> videojuegos = new ArrayList<>();


        String sql = """
        SELECT
            v.id,
            v.titulo,
            v.anio_lanzamiento,
            v.pegi,
            v.id_desarrolladora,
            d.nombre AS nombre_desarrolladora,
            COUNT(c.id) AS copias_totales,
            COUNT(
                CASE
                    WHEN c.activa = 1
                    AND NOT EXISTS (
                        SELECT 1
                        FROM prestamos p
                        WHERE p.id_copia = c.id
                        AND p.fecha_devolucion IS NULL
                    )
                    THEN 1
                END
            ) AS copias_disponibles
        FROM videojuegos v
        INNER JOIN desarrolladoras d
            ON v.id_desarrolladora = d.id
        LEFT JOIN copias c
            ON v.id = c.id_videojuego
        GROUP BY
            v.id,
            v.titulo,
            v.anio_lanzamiento,
            v.pegi,
            v.id_desarrolladora,
            d.nombre
        ORDER BY v.id """;


        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                VideojuegoDTO videojuego = new VideojuegoDTO(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("anio_lanzamiento"),
                        rs.getInt("pegi"),
                        rs.getInt("id_desarrolladora")

                );
                videojuego.setNombreDesarrolladora(
                        rs.getString("nombre_desarrolladora")
                );

                videojuego.setCopiasTotales(
                        rs.getInt("copias_totales")
                );

                videojuego.setCopiasDisponibles(
                        rs.getInt("copias_disponibles")
                );
                videojuegos.add(videojuego);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Error al consultar los videojuegos", e
            );
        }

        return videojuegos;
    }
}
