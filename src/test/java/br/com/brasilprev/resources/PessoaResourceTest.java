package br.com.brasilprev.resources;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.Test;

import br.com.brasilprev.ApplicationTests;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class PessoaResourceTest extends ApplicationTests {

	@Test
	public void deve_procurar_pessoa_pelo_ddd_e_numero_do_telefone() {
		get(this.getUri() + "/pessoas/41/999570146").then().statusCode(200).body("nome", equalTo("Iago"));
	}

	@Test
	public void deve_retornar_erro_nao_encontrado_quando_buscar_pessoa_por_telefone_inexistente() throws Exception {
		get(this.getUri() + "/pessoas/41/999570146111").then().statusCode(404);
	}

	@Test
	public void deve_salvar_nova_pessoa_no_sistema() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("nome", "Will");
		requestParams.put("cpf", "38767897123");
		JSONArray enderecos = new JSONArray();
		JSONObject endereco = new JSONObject();
		endereco.put("logradouro", "Rua dos Gerânios");
		endereco.put("numero", 497);
		endereco.put("complemento", "XXXX");
		endereco.put("bairro", "Pricumã");
		endereco.put("cidade", "Boa Vista");
		endereco.put("estado", "RR");
		enderecos.add(endereco);
		requestParams.put("enderecos", enderecos);
		JSONArray telefones = new JSONArray();
		JSONObject telefone = new JSONObject();
		telefone.put("ddd", "41");
		telefone.put("numero", "99999999");
		telefones.add(telefone);
		requestParams.put("telefones", telefones);

		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(this.getUri() + "/pessoas").then().statusCode(201)
				.body(containsString("Will"));
	}

	@Test
	public void deve_salvar_suas_pessoas_com_o_mesmo_cpf() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("nome", "Will");
		requestParams.put("cpf", "38767897100");
		JSONArray enderecos = new JSONArray();
		JSONObject endereco = new JSONObject();
		endereco.put("logradouro", "Rua dos Gerânios");
		endereco.put("numero", 497);
		endereco.put("complemento", "XXXX");
		endereco.put("bairro", "Pricumã");
		endereco.put("cidade", "Boa Vista");
		endereco.put("estado", "RR");
		enderecos.add(endereco);
		requestParams.put("enderecos", enderecos);
		JSONArray telefones = new JSONArray();
		JSONObject telefone = new JSONObject();
		telefone.put("ddd", "41");
		telefone.put("numero", "99999999");
		telefones.add(telefone);
		requestParams.put("telefones", telefones);

		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(this.getUri() + "/pessoas").then().statusCode(400);
	}

	@Test
	public void deve_salvar_suas_pessoas_com_o_mesmo_telefone() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("nome", "Will");
		requestParams.put("cpf", "38767897129");
		JSONArray enderecos = new JSONArray();
		JSONObject endereco = new JSONObject();
		endereco.put("logradouro", "Rua dos Gerânios");
		endereco.put("numero", 497);
		endereco.put("complemento", "XXXX");
		endereco.put("bairro", "Pricumã");
		endereco.put("cidade", "Boa Vista");
		endereco.put("estado", "RR");
		enderecos.add(endereco);
		requestParams.put("enderecos", enderecos);
		JSONArray telefones = new JSONArray();
		JSONObject telefone = new JSONObject();
		telefone.put("ddd", "82");
		telefone.put("numero", "39945903");
		telefones.add(telefone);
		requestParams.put("telefones", telefones);

		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(this.getUri() + "/pessoas").then().statusCode(400);
	}

	@Test
	public void deve_fitrar_pessoas_pelo_nome() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("nome", "Bre");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(this.getUri() + "/pessoas/filtrar").then().statusCode(200)
				.body(containsString("Breno"));
	}
}
