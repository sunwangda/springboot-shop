//package com.shop.user.client.dao;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SearchServiceImpl implements SearchService {
//    @Autowired
//    private GoodsRepository goodsRepository;
//    @Autowired
//    private ElasticsearchTemplate template;
//    @Override
//    
//    public PageResult<Goods> search(SearchRequest request) {
//        int page = request.getPage() - 1;
//        int size = request.getSize();
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        
//        //创建查询构建器
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//        //结果过滤
//        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "name"}, null));
//        //分页
//        queryBuilder.withPageable(PageRequest.of(page, size));
//        //过滤
//        String[] str = new String[2];
//        str[0] = "name";
//        str[1] = "brand";
//        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(request.getKey(), str));
//        //查询
//        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
//        //解析结果
//        //分页结果解析
//        long total = result.getTotalElements();
//        Integer totalPages1 = result.getTotalPages();    //失效
//        Long totalPages = total % size == 0 ? total / size : total / size + 1;
//        List<Goods> goodsList = result.getContent();
//        //解析聚合结果
//        return new PageResult(total, totalPages, goodsList);
//     }
//}
