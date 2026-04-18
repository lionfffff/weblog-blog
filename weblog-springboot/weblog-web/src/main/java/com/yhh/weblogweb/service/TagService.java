package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.tag.FindTagArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.tag.FindTagListReqVO;

public interface TagService {
    Response findTagList(FindTagListReqVO findTagListReqVO);

    Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO);
}
