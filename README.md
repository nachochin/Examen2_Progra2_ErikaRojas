# Sistema de Parqueo Publico

Proyecto desarrollado en Java con Swing para administrar el ingreso y salida de vehiculos en un parqueo publico.

## Funcionalidades

- Registro de ingreso de vehiculos por placa y tipo.
- Validacion de placa vacia y duplicada.
- Registro de salida con calculo automatico por hora o fraccion.
- Historial de registros finalizados.
- Eliminacion individual y limpieza total del historial.
- Reimpresion de recibo desde el historial.
- Persistencia de datos en archivos de texto dentro de `data/`.

## Estructura

```text
/src
   /entidades
   /logica
   /accesodatos
   /presentacion
/prompts.txt
/CHANGELOG.md
/README.md
```

## Tecnologias

- Java 21
- Java Swing
- NetBeans / Ant

## Ejecucion

1. Abrir el proyecto en NetBeans.
2. Ejecutar la clase principal del proyecto desde NetBeans.
3. Registrar ingresos y salidas desde la interfaz grafica.

## Datos del sistema

- `data/activos.txt`: vehiculos actualmente en el parqueo.
- `data/historial.txt`: historial de vehiculos retirados.

## Tarifa

La tarifa configurada actualmente es de `800` colones por hora o fraccion.

## Versiones

- `v1.0`: registro de entrada funcionando.
- `v1.1`: implementacion de salida y calculo del monto.
- `v1.2`: mejoras en documentacion y preparacion de la entrega.
