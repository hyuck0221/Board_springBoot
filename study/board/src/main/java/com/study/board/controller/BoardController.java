package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boardwrite";
    }
    @PostMapping("/board/writepro") //위치 이름 같아야 함
    public String boardWritePro(Board board, Model model){  //String title, String content으로 선언해도 되지만
                                                // 많아지면 불편함으로 만든 Board 이용
        boardService.write(board);  //데이터베이스에 적용
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }
    @GetMapping("/board/list")
    public String boardList(Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> list = boardService.boardList((pageable));
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);  //1보다 작을 시 1을 적용
        int endPage = Math.min(nowPage + 5, list.getTotalPages());  //페이지 수를 넘기면 페이지 수 적용

        model.addAttribute("list", list);
        model.addAttribute("nowpage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", boardService.boardList(pageable));
        return "boardlist";
    }
    @GetMapping("/board/view")  //localhost:8080/board/view?id=1 :: 1번 게시판 주소
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelect(Integer id){
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board) {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write((boardTemp));
        return "redirect:/board/list";
    }
}
