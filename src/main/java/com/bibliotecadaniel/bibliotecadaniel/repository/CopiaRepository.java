
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.CopiaDisponibleDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CopiaRepository {

    private final DataSource dataSource;

    public CopiaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CopiaDisponibleDTO> listarDisponibles() {

        List<CopiaDisponibleDTO> copias = new ArrayList<>();

        String sql = """
                SELECT
                    c.id,
                    v.titulo,
                    p.nombre AS plataforma,
                    c.codigo_inventario
                FROM copias c
                JOIN videojuegos v ON c.id_videojuego = v.id
                JOIN plataformas p ON c.id_plataforma = p.id
                WHERE c.activa = 1
                AND NOT EXISTS (
                    SELECT 1
                    FROM prestamos pr
                    WHERE pr.id_copia = c.id
                    AND pr.fecha_devolucion IS NULL
                )
                ORDER BY v.titulo
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                CopiaDisponibleDTO copia = new CopiaDisponibleDTO(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("plataforma"),
                        rs.getString("codigo_inventario")
                );

                copias.add(copia);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al listar las copias disponibles", e);
        }

        return copias;
    }
}
