package kr.ac.kopo.guestbook2024.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// View 클래스에서 화면에 출력하기 위해 필요한 정보들이 저장되는 클래스
@Data
public class PageResultDTO<DTO, EN> {
    // 화면에 보여질 글 목록: GuestbookDTO 객체 참조값들이 저장된 리스트
    // DTO 리스트
    private List<DTO> dtoList;
    // 총 페이지 번호
    private int totalPage;
    // 현재 페이지 번호
    private int page;
    // 한 페이지에 보여지는 글목록 개수
    private int size;
    // 페이지에 보여질 시작 페이지 번호
    private int start;
    // 페이지에 보여질 끝 페이지 번호
    private int end;
    // 화면을 이동할 이전, 다음 링크가 보여지거나 보이지 않게 설정
    private boolean prev, next;
    // 한 화면에 보여질 페이지 번호 목록이 저장
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // 매개변수로 전달받은 결과행들과 Entity를 DTO로
        // 변환한 fn을 사용해서 list에 GuestbookDTO 객체를 저장한 리스트
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages(); // 381개의 행을 갖고 있다면 31페이지이다.
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        // 0부터 시작하니 1을 추가
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        // 현재 화면에 보여질 임시 마지막 페이지 번호
        // 현재 페이지를 기준으로 화면에 출력되어야 하는 마지막 페이지 번호를 우선 처리
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9; // 시작 페이지 번호 처리
        // 실제 데이터가 부족한 경우를 위해 마지막 페이지 번호는 전체 데이터의 개수를 이용해서 다시 계산
        // 삼항조건연산자에서 조건식이 true면 마지막 화면이 아닌 경우 false면 마지막 화면이라는 의미
        // 전체페이지번호가 31일 때: 마지막 화면이 아닌 경우 1 ~ 3번째 화면(10, 20, 30), 마지막 화면은 4번째 화면을 의미(31)
        end = totalPage > tempEnd ? tempEnd : totalPage;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        // 기타 필요한 이전 페이지 여부
        prev = start > 1; // 2~ 마지막 화면까지 true
        // 다음 페이지 존재 여부 계산
        next = totalPage > tempEnd; // 1~ 마지막 바로 전화면까지 true
    }
}
