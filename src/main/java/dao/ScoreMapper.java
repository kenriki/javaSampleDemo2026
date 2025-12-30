package dao;
import java.util.List;

import entity.ScoreEntity;

public interface ScoreMapper {
    List<ScoreEntity> findAll();
    void insert(ScoreEntity score);
    void delete(int id);
}