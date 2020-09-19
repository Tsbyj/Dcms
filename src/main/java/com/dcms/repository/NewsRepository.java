package com.dcms.repository;

import com.dcms.pojo.news.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository {
    List<News> findAll(Integer page, Integer limit);

    Integer getNextId(String nowTime);

    Integer save(News news);

    Integer updateById(News news);

    Integer deleteById(String newsId);

    Integer getCount();
    //获取status=1的新闻
    List<News> getAll();

    List<News> findByHeadLine(String headLine);

    News findById(String newsId);

    List<News> findOther(String newsId);
}
