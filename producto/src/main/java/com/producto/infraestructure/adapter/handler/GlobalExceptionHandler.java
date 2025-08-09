package com.producto.infraestructure.adapter.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
    }*/
    //mensaje en texto plano
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body("Error: " + ex.getMessage());
    }
    /*
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }*/
    //mensaje en texto plano
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        //StringBuilder mensaje = new StringBuilder("Errores de validación:\n");
        StringBuilder mensaje = new StringBuilder("");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            mensaje.append("- ")
                    //.append(error.getField())
                    //.append(": ")
                    .append(error.getDefaultMessage())
                    .append("\n");
        });

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(mensaje.toString());
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    /*
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumParseErrors(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        String mensaje = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        error.put("error", "Error al leer el cuerpo de la solicitud");
        error.put("detalle", mensaje);

        return ResponseEntity.badRequest().body(error);
    }*/
    //mensaje en texto plano
    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleEnumParseErrors(HttpMessageNotReadableException ex) {
        String mensaje = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        String cuerpo = "Error: " + mensaje;

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(cuerpo);
    }*/

    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleEnumParseErrors(HttpMessageNotReadableException ex) {
        String mensajeOriginal = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        String cuerpo;

        // Intentamos extraer el valor inválido y los valores válidos del enum
        if (mensajeOriginal.contains("Cannot deserialize value of type") &&
                mensajeOriginal.contains("not one of the values accepted for Enum class")) {

            // Extraer el valor inválido
            String valorInvalido = mensajeOriginal.split("from String \"")[1].split("\"")[0];

            // Extraer los valores válidos
            String valoresValidos = mensajeOriginal.split("Enum class: ")[1].split("]")[0] + "]";

            cuerpo = "Valor inválido para el campo 'tipoAlmacenamiento': \"" + valorInvalido +
                    "\". Valores permitidos: " + valoresValidos;
        } else if(mensajeOriginal.contains("java.lang.Integer") &&
                  mensajeOriginal.contains("Cannot deserialize value of type")) {
            cuerpo = "El campo "+extraerTextoEntreCorchetes(mensajeOriginal)+" debe ser un número.";
        } else {
            cuerpo = "Error al leer el cuerpo de la solicitud.";
        }

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(cuerpo);
        }*/
    /*Extrae de mensaje una cadena de texto el unico valor de texto que tiene una estructura [" "] */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        String mensaje = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        String cuerpo;

        if (mensaje.contains("Unexpected character")) {
            cuerpo = "Error de sintaxis en el JSON: verifica comas y comillas.";
        }else if (mensaje.contains("Unrecognized token")) {
            cuerpo = "Token no reconocido. Verifique que los campos tengan valores válidos.";
        }else if (mensaje.contains("Cannot deserialize value of type") &&
                mensaje.contains("not one of the values accepted for Enum class")) {
            String valorInvalido = mensaje.split("from String \"")[1].split("\"")[0];
            String valoresValidos = mensaje.split("Enum class: ")[1].split("]")[0] + "]";
            cuerpo = "Valor inválido para el campo 'tipoAlmacenamiento': \"" + valorInvalido +
                    "\". Valores permitidos: " + valoresValidos;
        } else if (mensaje.contains("java.lang.Integer") &&
                mensaje.contains("Cannot deserialize value of type")) {
            cuerpo = "El campo " + extraerTextoEntreCorchetes(mensaje) + " debe ser un número.";
        }else if (mensaje.contains("java.math.BigDecima") &&
                mensaje.contains("Cannot deserialize value of type")) {
            cuerpo = "El campo " + extraerTextoEntreCorchetes(mensaje) + " debe ser un número decimal o entero.";
        } else {
            cuerpo = "Error al leer el cuerpo de la solicitud: " + mensaje;
        }
        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(cuerpo);


    }

    public String extraerTextoEntreCorchetes(String texto) {
        Pattern pattern = Pattern.compile("\\[\"(.*?)\"\\]");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /*
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDeserializationError(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error en el formato de los datos: asegúrate de que los campos como 'stock' y 'vendidos' sean números válidos. Detalles: " + ex.getMostSpecificCause().getMessage());
    }*/

    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumParseErrors(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        Throwable cause = ex.getCause();

        if (cause instanceof IllegalArgumentException && cause.getMessage() != null) {
            // Error típico al convertir un valor inválido a enum
            error.put("error", "Valor inválido para un campo tipo enum: " + cause.getMessage());
        } else {
            // Otro tipo de error de lectura del cuerpo
            error.put("error", "Error al leer la solicitud: " + ex.getMessage());
        }

        return ResponseEntity.badRequest().body(error);
    }*/


}
