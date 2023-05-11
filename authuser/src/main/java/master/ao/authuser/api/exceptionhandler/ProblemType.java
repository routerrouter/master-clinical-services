package master.ao.authuser.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	INVALID_DATA("/dados-invalidos", "Dados inválidos"),
	DENIED_ACCESS("/acesso-negado", "Acesso negado"),
	SYSTEM_ERROR("/erro-de-sistema", "Erro de sistema"),
	INVALID_PARAMETER("/parametro-invalido", "Parâmetro inválido"),
	MESSAGE_INCOMPREENSIBLE("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RESOURCE_NOT_FOUND("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTITY_IN_USE("/entidade-em-uso", "Entidade em uso"),
	DUPLICATED_DATA("/dado-duplicado", "Informação duplicada"),
	BUSSINES_ERROR("/erro-negocio", "Violação de regra de negócio"),
	EXPIRED_ACCOUNT("/erro-acesso","Sua conta encontra-se expirada"),
	BAD_CREDENTIALS("/erro-acesso","Credenciais informadas estão erradas"),
	ACCOUNT_ACCESS_LIMIT("/erro-acesso","Limite de acesso da conta excedido."),
	BLOCKED_ACCOUNT("/erro-acesso","Sua conta encontra-se bloqueada");
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://socompser.co.ao" + path;
		this.title = title;
	}
	
}