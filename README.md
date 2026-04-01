🚀 Spring Boot + Resilience4j (Circuit Breaker POC)

Este projeto é um Proof of Concept (POC) para demonstrar o uso de Circuit Breaker com Resilience4j em uma aplicação Spring Boot.

A ideia principal é simular um cenário real de microserviços onde:

um serviço cliente faz requisições para outro serviço (product-service)
o Circuit Breaker protege a aplicação contra falhas externas
um fallback é aplicado quando o serviço está indisponível
🧠 Conceito

Este projeto representa o lado consumidor (client-service).

Ele faz chamadas HTTP para outro microserviço via Feign Client e utiliza Resilience4j para:

evitar chamadas repetidas para serviços instáveis
proteger o sistema contra cascata de falhas
fornecer resposta fallback quando necessário
🏗️ Arquitetura
Client Service (este projeto)
        ↓
Feign Client
        ↓
Product Service (mock ou outro ms)
⚙️ Configuração
application.yml
server:
  port: 8080

spring:
  application:
    name: client-service

services:
  product:
    url: http://localhost:8081

resilience4j.circuitbreaker:
  instances:
    clientCB:
      minimumNumberOfCalls: 4
      slidingWindowSize: 8
🔌 Integração com Feign Client
@FeignClient(
        name = "product-service",
        url = "${services.product.url}"
)
public interface ProductClient {

    @GetMapping("/products")
    ProductResponse getProduct();
}
🔁 Circuit Breaker com Fallback
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ProductClient client;
    private final ProductMapper mapper;

    @CircuitBreaker(name = "clientCB", fallbackMethod = "getProductFallback")
    public Product getProduct() {
        return mapper.toDomain(client.getProduct());
    }

    public Product getProductFallback(Throwable t) {
        Product product = new Product();
        product.setName("Produto fallback");
        product.setPrice("0");
        return product;
    }
}
🌐 Endpoint
@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/products")
    public Product product() {
        return clientService.getProduct();
    }
}

👉 Acesse:

GET http://localhost:8080/products
📦 Modelo de resposta
@Data
public class ProductResponse {
    private String name;
    private String price;
}
📊 Logs do Circuit Breaker

O projeto também inclui monitoramento de transições de estado:

@Configuration
public class CircuitBreakerConfig {

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfig.class);

    @Bean
    public RegistryEventConsumer<CircuitBreaker> cbLog(){
        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry()
                        .getEventPublisher()
                        .onStateTransition(event -> logger.info(event.toString()));
            }
        };
    }
}

👉 Você verá logs como:

CLOSED → OPEN
OPEN → HALF_OPEN
📚 Dependência principal
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
🧪 Como testar
1. Suba um serviço mock (ou outro microserviço)

Exemplo simples:

http://localhost:8081/products
2. Rode o client-service
3. Teste comportamento:
✅ Serviço funcionando:

Retorna resposta real

❌ Serviço fora:

Após algumas falhas:

Circuit Breaker abre
Retorna fallback:
{
  "name": "Produto fallback",
  "price": "0"
}
🎯 Objetivo do projeto

Este projeto foi criado para:

estudar Resilience4j na prática
entender comportamento de Circuit Breaker
simular falhas entre microserviços
aplicar fallback de forma controlada
🚀 Possíveis evoluções
Retry + Circuit Breaker
Rate Limiter
Bulkhead
Métricas com Micrometer + Prometheus
Dashboard com Grafana
Logs estruturados (ex: ELK / Datadog)
💡 Insight importante

Esse tipo de padrão é essencial em sistemas distribuídos.

Sem Circuit Breaker:

seu sistema pode cair junto com dependências externas

Com Circuit Breaker:

você controla falhas
protege seu sistema
melhora resiliência
