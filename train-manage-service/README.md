# Train Manage System

### Полезные команды

Создание топика в CLI
```shell
kafka-topics --bootstrap-server localhost:29092 --topic train_manage_system --create
```

Создание продюсера в CLI
```shell
kafka-console-producer --broker-list localhost:29092 --topic train_manage_system --property parse.key=true --property key.separator=' '
```
Создание консьюмера в CLI
```shell
kafka-console-consumer --bootstrap-server localhost:29092 --topic train_manage_system_ans --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true
```
```shell
kafka-console-consumer --bootstrap-server localhost:29092 --topic train_manage_system_ans --from-beginning
```
