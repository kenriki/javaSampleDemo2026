package service;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import dao.ScoreMapper;
import entity.ScoreEntity;

public class ScoreService {
    private SqlSessionFactory sessionFactory;

    // コンストラクタで、作成済みの sessionFactory を受け取るようにします
    public ScoreService(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ScoreEntity> getAllScores() throws Exception {
        try (SqlSession session = sessionFactory.openSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            return mapper.findAll();
        }
    }

    public void registerScore(ScoreEntity score) throws Exception {
        if (score.getSubjectName() == null || score.getSubjectName().isEmpty()) {
            throw new Exception("学習項目が空です");
        }
        try (SqlSession session = sessionFactory.openSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            mapper.insert(score);
            session.commit(); 
        }
    }
    
    public void deleteScore(int id) throws Exception {
        try (SqlSession session = sessionFactory.openSession()) {
            ScoreMapper mapper = session.getMapper(ScoreMapper.class);
            mapper.delete(id);
            session.commit();
        }
    }
}