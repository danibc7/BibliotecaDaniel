
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

    public List<VideojuegoDTO> buscarPorTitulo(String titulo, Integer idDesarrolladora, Integer anioDesde,Integer pegiMaximo, boolean soloDisponibles) {

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
                WHERE LOWER(v.titulo) LIKE LOWER(?)
                AND (? IS NULL OR v.id_desarrolladora = ?)
                AND (? IS NULL OR v.anio_lanzamiento >= ?)
                AND (? IS NULL OR v.pegi <= ?)
                GROUP BY
                    v.id,
                    v.titulo,
                    v.anio_lanzamiento,
                    v.pegi,
                    v.id_desarrolladora,
                    d.nombre
                HAVING (? = 0 OR copias_disponibles > 0)
                ORDER BY v.id
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + (titulo == null ? "" : titulo) + "%");
            if (idDesarrolladora == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, idDesarrolladora);
                ps.setInt(3, idDesarrolladora);
            }

            if (anioDesde == null) {
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, anioDesde);
                ps.setInt(5, anioDesde);
            }
            if (pegiMaximo == null) {
                ps.setNull(6, java.sql.Types.INTEGER);
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, pegiMaximo);
                ps.setInt(7, pegiMaximo);
            }
            ps.setBoolean(8, soloDisponibles);


            try (ResultSet rs = ps.executeQuery()) {

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
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Error al consultar los videojuegos", e
            );
        }

        return videojuegos;
    }

    public List<VideojuegoDTO> listarTodos() {
        return buscarPorTitulo("", null, null,null,false);
    }

    public void guardarVideojuego(String titulo, int anioLanzamiento, int pegi, int idDesarrolladora) {

        String sql = """
            INSERT INTO videojuegos
            (titulo, anio_lanzamiento, pegi, id_desarrolladora)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, titulo);
            ps.setInt(2, anioLanzamiento);
            ps.setInt(3, pegi);
            ps.setInt(4, idDesarrolladora);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el videojuego", e);
        }
    }

}
