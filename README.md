# kafka-streams-demo

## Anotações
Kafka streams (curso udemy)

### properties:
  application.id = group.id do consumidor
  default.[key|value].serde -> é pra serialização e deserialização

### topologia:
É um grafo de processors ligados entre si por streams, isto é, na api kafka streams existe um stream, uma sequencia de dados 
imutáveis e ordanados que podem ser consumidos mais de uma vez, e o stream processor, um nó no grafo que transforma os streams 
consumidos, dado por dado, e pode criar novos streams a partir desses dados. Portanto a topologia é um grafo onde os nós 
são os processors e as vértices são os streams.

Tipos de processors:
- source processor = nós que consomem os dados de um tópico do kafka e são os primeiros a produzirem streams de dados dentro
  da topologia
- sink processor = são nós que apenas consomem streams e produzem dados em um tópico do kafka, são os últimos processors dentro
  da topolgia do kafka streams

#### Exemplo do curso
1º consumir o stream de dados, no kafka streams os dados são estruturas de chave|valor

2º mapear os valores do stream, por exemplo, para lower case 

3º flat map os valores, por exemplo, separando os dados a partir do espaço entre as palavras:
  <"key", "value kafka streams"> -> <"key1", "value">; <"key2", "kafka">; <"key3", "streams">

4º SelectKey para aplicar chaves aos values, por exemplo:
  <"value", "value">; <"kafka", "kafka">; <"streams", "streams">
  ... (ai continua conforme a proposta do exemplo usado durante o curso - é o mesmo exemplo no site oficial do Apache Kafka)


### Internal topics no Kafka Streams:
São tópicos criados pela propria API do kafka streams para controle interno dos dados duranto o processamento dos mesmos. E são de dois tipos:
- Repartionating topic: quando vc transforma/muda a chave do stream<key, value>, os dados são reparticionados usando esses topicos internos;
- Changelog topics: quando se faz a agregação dos dados vindo do streams<key, value>, os dados são salvos nesses topicos durante o processo;

São gerenciados pela Api Kafka Streams e utilizados para salvar/restaurar/reparticionar os dados e todos esses topicos tem como prefixo o APPICATION.ID, 
por isso é muito importante definir essa property e tomar cuidado ao alterar ela (ja que ela tb é usado como group.id dos consumidores dos topicos do kafka streams). 
E também não dever ser manuseados pelo dev ou pela própria aplicação, somente pela api kafka streams.
