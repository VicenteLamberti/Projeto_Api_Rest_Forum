package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.DetalhesTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

//	@RequestMapping("/topicos")
//	public List<TopicoDto> lista(){
//		List<Topico> topicos = topicoRepository.findAll();
//		return TopicoDto.converter(topicos);
//	}
	
	
//	Sem paginacao
//	@GetMapping
//	public List<TopicoDto> lista(String nomeCurso) {
//		
//		
//		
//		if (nomeCurso == null) {
//			List<Topico> topicos = topicoRepository.findAll();
//			return TopicoDto.converter(topicos);
//		} else {
//			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
//			return TopicoDto.converter(topicos);
//		}
//
//	}

	
//	Com paginacao mas sem ordenacao
//	@GetMapping
//	public Page<TopicoDto> lista(@RequestParam(required=false)String nomeCurso,
//			@RequestParam int pagina, @RequestParam int qtd) {
//		
//		Pageable paginacao = PageRequest.of(pagina, qtd);
//		
//		if (nomeCurso == null) {
//			Page<Topico> topicos = topicoRepository.findAll(paginacao);
//			return TopicoDto.converter(topicos);
//		} else {
//			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
//			return TopicoDto.converter(topicos);
//		}
//
//	}
	
	
//	Com paginacao e ordenacao
//	@GetMapping
//	public Page<TopicoDto> lista(@RequestParam(required=false)String nomeCurso,
//			@RequestParam int pagina, 
//			@RequestParam int qtd,
//			@RequestParam String ordenacao) {
//		
//		Pageable paginacao = PageRequest.of(pagina, qtd,Direction.ASC, ordenacao);
//		
//		if (nomeCurso == null) {
//			Page<Topico> topicos = topicoRepository.findAll(paginacao);
//			return TopicoDto.converter(topicos);
//		} else {
//			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
//			return TopicoDto.converter(topicos);
//		}
//
//	}
	
	
//	Com paginacao e ordenacao, mas recebendo apenas um Pageable no parametro, e nao todas aquelas informacoes
	@GetMapping
	@Cacheable(value="listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required=false)String nomeCurso,
			@PageableDefault(sort = "id")Pageable paginacao) {
		
		
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
			return TopicoDto.converter(topicos);
		}

	}

	@PostMapping
	@Transactional
	@CacheEvict(value="listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

//	Assim não tem tratamento nenhum
//	@GetMapping("/{id}")
//	public DetalhesTopicoDto detalhar(@PathVariable("id") Long codigo) {
//		Optional<Topico> topico = topicoRepository.findById(codigo);
//		return new DetalhesTopicoDto(topico.orElse(null));
//	}

	
//	Assim o tratamento está sendo feito aqui
//	@GetMapping("/{id}")
//	public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable("id") Long codigo) {
//		Optional<Topico> topico = topicoRepository.findById(codigo);
//		if(topico.isPresent()) {
//			return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
//		}
//		return ResponseEntity.notFound().build();
//	}

	
//	Assim caso não encontrar, vai ser tratado dentro do Handler
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable("id") Long codigo) {
		Optional<Topico> topico = topicoRepository.findById(codigo);
		return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));

	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value="listaDeTopicos",allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Topico topico = form.atualizar(id, topicoRepository);
//		return ResponseEntity.ok(new TopicoDto(topico));
		return ResponseEntity.ok().body(new TopicoDto(topico));
	}

//	Retorna 200
//	@DeleteMapping("{id}")
//	@Transactional
//	public ResponseEntity<?> excluir(@PathVariable Long id) {
//		topicoRepository.deleteById(id);
//		return ResponseEntity.ok().build();
//	}

//	Retorna 204
	@DeleteMapping("{id}")
	@Transactional
	@CacheEvict(value="listaDeTopicos",allEntries = true)
	public ResponseEntity<TopicoDto> excluir(@PathVariable Long id) {
		topicoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
