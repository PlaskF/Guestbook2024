package kr.ac.kopo.guestbook2024.repository;

import kr.ac.kopo.guestbook2024.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.range(1, 301).forEach(i -> {
           Guestbook guestbook = Guestbook.builder()
                   .title("Title====" + i)
                   .content("Content====" + i)
                   .author("Author====" + (i%10 + 1))
                   .build();
           guestbookRepository.save(guestbook);
        });
    }

    @Test
    public void updateDummies() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title .....");
            guestbook.changeContent("Changed Content .....");
            guestbookRepository.save(guestbook);
        }
    }
}
