package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(x -> {
			String mensagem = messageSource.getMessage(x, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(x.getField(),mensagem);
			dto.add(erro);
		});
		return dto;
	}
	
	
//	@ExceptionHandler(NoSuchElementException.class)
//	public ResponseEntity<?> handle(NoSuchElementException exception) {
//	
//		return ResponseEntity.notFound().build();
//	}
	
//	@ResponseStatus(code=HttpStatus.NOT_FOUND)
//	@ExceptionHandler(NoSuchElementException.class)
//	public String handle(NoSuchElementException exception) {
//	
//		return "Ops nada por aqui foi encontrado. Tente outro id";
//	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<?> handle(NoSuchElementException exception) {
	
		return new ResponseEntity<>("Ops nada por aqui foi encontrado",HttpStatus.NOT_FOUND);
	}
	
	
	
	
	
	
	
}
