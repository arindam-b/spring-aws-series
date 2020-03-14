package fr.arindam.aws.rds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.arindam.aws.rds.entity.Projects;

public interface ProjectRepository extends JpaRepository<Projects, Integer> {

}
