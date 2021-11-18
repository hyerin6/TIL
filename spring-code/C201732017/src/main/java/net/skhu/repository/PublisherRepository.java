package net.skhu.repository;

import net.skhu.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    List<Publisher> findAll();
}
