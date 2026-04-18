package com.yhh.weblogweb.markdown;

import com.yhh.weblogweb.markdown.renderer.ImageNodeRenderer;
import com.yhh.weblogweb.markdown.renderer.LinkNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * @Date 2025-06-30 21:52
 * @ClassName: MarkdownHelper
 * @Description: 自定义Markdown工具类
 */
public class MarkdownHelper {

    /**
     * Markdown 解析器
     */
    private static Parser PARSER;
    /**
     * HTML 渲染器
     */
    private static HtmlRenderer HTML_RENDERER;

    /**
     * 初始化
     */
    static {
        PARSER = Parser.builder().build();
        HTML_RENDERER = HtmlRenderer.builder().build();
    }
    static {
        // Markdown 拓展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(), // 表格拓展
                HeadingAnchorExtension.create(), // 标题锚定项
                ImageAttributesExtension.create(), // 图片宽高
                TaskListItemsExtension.create() // 任务列表
        );

        PARSER = Parser.builder().extensions(extensions).build();
        HTML_RENDERER = HtmlRenderer.builder()
                .extensions(extensions)
                .nodeRendererFactory(context -> new ImageNodeRenderer(context)) // 自定义图片解析
                .nodeRendererFactory(context -> new LinkNodeRenderer(context)) // 自定义超链接解析
                .build();
    }



    /**
     * 将 Markdown 转换成 HTML
     * @param markdown
     * @return
     */
    public static String convertMarkdown2Html(String markdown) {
        Node document = PARSER.parse(markdown);
        return HTML_RENDERER.render(document);
    }

//    public static void main(String[] args) {
//        String markdown = "This is *Sparta*";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//    }

    public static void main(String[] args) {//表格渲染
        String markdown = "| First Header  | Second Header |\n" +
                "| ------------- | ------------- |\n" +
                "| Content Cell  | Content Cell  |\n" +
                "| Content Cell  | Content Cell  |";
        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
    }


}
