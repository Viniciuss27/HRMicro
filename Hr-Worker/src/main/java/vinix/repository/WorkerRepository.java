package vinix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{

}
