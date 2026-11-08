
package com.bibliotecadaniel.bibliotecadaniel.repository;

import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DesarrolladoraRepository {

    private final DataSource dataSource;

    public DesarrolladoraRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<DesarrolladoraDTO> listarTodas() {

        List<DesarrolladoraDTO> desarrolladoras = new ArrayList<>();

        String sql = "SELECT id, nombre FROM desarrolladoras ORDER BY nombre";

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                DesarrolladoraDTO desarrolladora = new DesarrolladoraDTO(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );

                desarrolladoras.add(desarrolladora);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Error al consultar las desarrolladoras", e
            );
        }

        return desarrolladoras;
    }

    public boolean existePorId(int idDesarrolladora) {

        String sql = "SELECT COUNT(*) FROM desarrolladoras WHERE id = ?";

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idDesarrolladora);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al comprobar la desarrolladora", e);
        }

        return false;
    }

}
