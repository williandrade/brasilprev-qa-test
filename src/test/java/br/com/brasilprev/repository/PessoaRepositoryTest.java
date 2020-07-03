package br.com.brasilprev.repository;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.brasilprev.Application;
import net.minidev.json.JSONObject;

@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class PessoaRepositoryTest {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void deve_procurar_pessoa_pelo_cpf() {
		get(template.getRootUri() + "/pessoas/cpf/86730543540").then().statusCode(200).body("nome", equalTo("Iago"));
	}

	@Test
	public void deve_encontrar_pessoa_cpf_inexistente() {
		get(template.getRootUri() + "/pessoas/cpf/8673054354011").then().statusCode(404);
	}

	@Test
	public void deve_encontrar_pessoa_pelo_ddd_e_numero_de_telefone() {
		get(template.getRootUri() + "/pessoas/41/999570146").then().statusCode(200).body("nome", equalTo("Iago"));
	}

	@Test
	public void nao_deve_encontrar_pessoa_cujo_ddd_e_telefone_nao_estejam_cadastradados() {
		get(template.getRootUri() + "/pessoas/41/99957014612").then().statusCode(404);
	}

	@Test
	public void deve_filtrar_pessoas_por_parte_do_nome() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("nome", "Bre");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(template.getRootUri() + "/pessoas/filtrar").then()
				.statusCode(200).body(containsString("Breno"));
	}

	@Test
	public void deve_filtrar_pessoas_por_parte_do_cpf() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("cpf", "8673054");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(template.getRootUri() + "/pessoas/filtrar").then()
				.statusCode(200).body(containsString("Iago"));
	}

	@Test
	public void deve_filtrar_pessoas_por_filtro_composto() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("cpf", "8673054");
		requestParams.put("nome", "Ia");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(template.getRootUri() + "/pessoas/filtrar").then()
				.statusCode(200).body(containsString("Iago"));
	}

	@Test
	public void deve_filtrar_pessoas_pelo_ddd_do_telefone() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("ddd", "41");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(template.getRootUri() + "/pessoas/filtrar").then()
				.statusCode(200).body(containsString("Iago"));
	}

	@Test
	public void deve_filtrar_pessoas_pelo_numero_do_telefone() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("telefone", "999570146");
		given().body(requestParams.toJSONString()).header("Content-Type", "application/json")
				.header("Accept", "application/json").post(template.getRootUri() + "/pessoas/filtrar").then()
				.statusCode(200).body(containsString("Iago"));
	}
}
