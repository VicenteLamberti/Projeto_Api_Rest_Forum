package br.com.alura.forum.repository;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.modelo.Curso;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CursoRepositoryTest {

	@Autowired
	private CursoRepository repository;
	
	@Autowired
	private TestEntityManager em;
	
//	Assim é sem o entity manager, com o banco ja populado
//	@Test
//	public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
//		String nomeCurso = "HTML 5";
//		Curso curso = repository.findByNome(nomeCurso);
//		Assert.assertNotNull(curso);
//		Assert.assertEquals(nomeCurso, curso.getNome());
//	}
//	
//	@Test
//	public void naoDeveriaCarregarUmCursoCujoNomeNaoEstejaCadastrado() {
//		String nomeCurso = "JPA";
//		Curso curso = repository.findByNome(nomeCurso);
//		Assert.assertNull(curso);
//	}
	

	@Test
	public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
		String nomeCurso = "HTML 5";
		Curso html5 = new Curso();
		html5.setNome(nomeCurso);
		html5.setCategoria("Programação");
	

		em.persist(html5);
		
		
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNotNull(curso);
		Assert.assertEquals(nomeCurso, curso.getNome());
	}
	
	@Test
	public void naoDeveriaCarregarUmCursoCujoNomeNaoEstejaCadastrado() {
		String nomeCurso = "JPA";
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNull(curso);
	}

}
