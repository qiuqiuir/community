package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther xurou
 * @date 2022/7/8
 */

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        }catch (IOException e) {
            logger.error("加载敏感词文件失败:" + e.getMessage());
        }
    }

    // 将敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            tempNode = tempNode.getSubNode(c);

        }
        tempNode.setKeyWordEnd(true);
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();
        while(begin < text.length()) {
            while(begin < text.length() && isSymbol(text.charAt(begin))) {
                sb.append(text.charAt(begin));
                begin=++position;
            }
            while(position < text.length()) {
                char c= text.charAt(position);
                // 跳过符号
                if (isSymbol(c)){
                    position++;
                    continue;
                }
                tempNode = tempNode.getSubNode(c);
                if (tempNode == null) {
                    sb.append(text.charAt(begin));
                    position=++begin;
                    tempNode = rootNode;
                    break;
                } else if(tempNode.isKeyWordEnd()){
                    sb.append(REPLACEMENT);
                    begin=++position;
                    tempNode = rootNode;
                    break;
                } else {
                    position++;
                }
            }
            if (begin !=text.length() && position == text.length()) {
                sb.append(text.charAt(begin));
                position=++begin;
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode{

        // 关键词结束表示
        private boolean isKeyWordEnd = false;

        // 子节点(key是下级字符,value是下级结点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();


        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
