package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @auther xurou
 * @date 2022/7/15
 */

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登陆哦!");
        }
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
