
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

        String sql = "SELECT id, titulo, anio_lanzamiento, pegi, " + "id_desarrolladora FROM videojuegos";

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
