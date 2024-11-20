package com.familiahuecas.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familiahuecas.backend.entity.Numeracion;
import com.familiahuecas.backend.repository.NumeracionRepository;
import com.familiahuecas.backend.rest.request.RecaudacionRequest;
import com.familiahuecas.backend.rest.response.NumeracionResponse;
import com.familiahuecas.backend.rest.response.RecaudacionResponse;
import com.familiahuecas.backend.rest.response.RecaudacionesResponse;

@Service
@Transactional
public class NumeracionService {
    private final NumeracionRepository numeracionRepository;

    public NumeracionService(NumeracionRepository numeracionRepository) {
        this.numeracionRepository = numeracionRepository;
    }

    public Page<NumeracionResponse> getAllPaginated(Pageable pageable, String orderBy, boolean isOrderDesc) {
        // Obtener las entidades paginadas desde el repositorio
        Page<Numeracion> numeraciones = numeracionRepository.findAll(pageable);

        // Convertir las entidades en NumeracionResponse
        return numeraciones.map(numeracion -> new NumeracionResponse(
                numeracion.getId(),
                numeracion.getEntradaM1(),
                numeracion.getSalidaM1(),
                numeracion.getEntradaM2(),
                numeracion.getSalidaM2(),
                numeracion.getBar(),
                numeracion.getFecha()
        ));
    }

	public void deleteNumeracionById(Long id) {
		 Numeracion user = numeracionRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Numeracion no encontrado con ID: " + id));

		 numeracionRepository.delete(user);
		
	}
	public RecaudacionResponse calculateRec(int entradaM1, int salidaM1, int entradaM2, int salidaM2) {
        // Recuperar el último registro de "Numeracion" donde bar = "lucy", ordenado por fecha descendente
        Optional<Numeracion> numeracionOptional = numeracionRepository
                .findTopByBarOrderByFechaDesc("lucy");

        if (numeracionOptional.isPresent()) {
            Numeracion numeracion = numeracionOptional.get();
            // Recuperar los valores del último registro
            int lastEntradaM1 = numeracion.getEntradaM1();
            int lastSalidaM1 = numeracion.getSalidaM1();
            int lastEntradaM2 = numeracion.getEntradaM2();
            int lastSalidaM2 = numeracion.getSalidaM2();
           
            // Realizar las restas
            int restaEntradaM1 = entradaM1 - lastEntradaM1;
            int restaSalidaM1 = salidaM1 - lastSalidaM1;
            int restaEntradaM2 = entradaM2 - lastEntradaM2;
            int restaSalidaM2 = salidaM2 - lastSalidaM2;
            
           

            // Calcular totalM1 y totalM2
            int totalM1 = restaEntradaM1 - restaSalidaM1;
            int totalM2 = restaEntradaM2 - restaSalidaM2;

           
            // Calcular el total general y total por cada uno
            float total = (float) ((totalM1 + totalM2)*0.2);
           
            float totalCadaUno = total / 2.0f;
           
            // Crear y devolver la respuesta
            RecaudacionResponse recaudacionResponse = 
            		new RecaudacionResponse(
            				entradaM1,
            				salidaM1,
            				entradaM2,
            				salidaM2,
            				lastEntradaM1,
            				lastSalidaM1,
            				lastEntradaM2,
            				lastSalidaM2,
            				restaEntradaM1,
            				restaSalidaM1,
            				restaEntradaM2,
            				restaSalidaM2,
            				total,
            				totalCadaUno,
            				totalM1,
            				totalM2);
            return recaudacionResponse;
        } else {
            throw new IllegalArgumentException("No se encontró ningún registro de numeración con bar = 'lucy'");
        }
    }
	 public void guardarRecaudacion(RecaudacionRequest recaudacionRequest) {
	        Numeracion numeracion = new Numeracion();
	        numeracion.setBar(recaudacionRequest.getBar());
	        numeracion.setEntradaM1(recaudacionRequest.getEntradaM1());
	        numeracion.setSalidaM1(recaudacionRequest.getSalidaM1());
	        numeracion.setEntradaM2(recaudacionRequest.getEntradaM2());
	        numeracion.setSalidaM2(recaudacionRequest.getSalidaM2());
	        numeracion.setFecha(java.time.LocalDateTime.now()); // Establece la fecha actual

	        numeracionRepository.save(numeracion);
	    }



	 public Page<RecaudacionesResponse> getAllRecaudcionesPaginated(Pageable sortedPageable, String orderBy, boolean isOrderDesc) {
		    // Recuperar las numeraciones de manera paginada y ordenadas
		    Page<Numeracion> numeracionesPage = numeracionRepository.findAll(sortedPageable);

//		    System.out.println("Numeraciones recuperadas:");
//		    numeracionesPage.forEach(numeracion -> {
//		        System.out.println("ID: " + numeracion.getId() + 
//		                           ", Bar: " + numeracion.getBar() + 
//		                           ", EntradaM1: " + numeracion.getEntradaM1() + 
//		                           ", Fecha: " + numeracion.getFecha());
//		    });

		    List<RecaudacionesResponse> recaudacionesResponses = new ArrayList<>();
		    List<Numeracion> numeraciones = numeracionesPage.getContent();

		    Numeracion registroAnterior = null;

		    // Iterar sobre las numeraciones y calcular con el registro anterior, omitiendo el primer registro
		    for (int i = 1; i < numeraciones.size(); i++) {
		        Numeracion numeracionActual = numeraciones.get(i);
		        registroAnterior = numeraciones.get(i - 1);

		        // Depurar los valores antes de calcular
//		        System.out.println("Cálculo entre:");
//		        System.out.println("Registro Actual ID: " + numeracionActual.getId() + ", EntradaM1: " + numeracionActual.getEntradaM1());
//		        System.out.println("Registro Anterior ID: " + registroAnterior.getId() + ", EntradaM1: " + registroAnterior.getEntradaM1());

		        // Calcular la recaudación con el registro anterior
		        RecaudacionesResponse recaudacion = calculateRec(numeracionActual, registroAnterior);
		        recaudacionesResponses.add(recaudacion);
		    }

		    // Convertir la lista de respuestas a un objeto Page
		    return new PageImpl<>(recaudacionesResponses, sortedPageable, numeracionesPage.getTotalElements());
		}

		// Método para calcular la recaudación en base al registro actual y el anterior
	 private RecaudacionesResponse calculateRec(Numeracion numeracionActual, Numeracion numeracionAnterior) {
		    // Recuperar los valores del registro actual y el anterior
		    int entradaM1 = numeracionActual.getEntradaM1();
		    int salidaM1 = numeracionActual.getSalidaM1();
		    int entradaM2 = numeracionActual.getEntradaM2();
		    int salidaM2 = numeracionActual.getSalidaM2();

		    int prevEntradaM1 = numeracionAnterior.getEntradaM1();
		    int prevSalidaM1 = numeracionAnterior.getSalidaM1();
		    int prevEntradaM2 = numeracionAnterior.getEntradaM2();
		    int prevSalidaM2 = numeracionAnterior.getSalidaM2();

		    // Realizar las restas para calcular las diferencias
		    int diffEntradaM1 = entradaM1 - prevEntradaM1;
		    int diffSalidaM1 = salidaM1 - prevSalidaM1;
		    int diffEntradaM2 = entradaM2 - prevEntradaM2;
		    int diffSalidaM2 = salidaM2 - prevSalidaM2;

		    // Calcular totalM1 y totalM2, asegurando que no sean negativos
		    int totalM1 = Math.abs(diffEntradaM1 - diffSalidaM1);
		    int totalM2 = Math.abs(diffEntradaM2 - diffSalidaM2);

		    // Multiplicar por 0.2 para obtener los valores finales
		    totalM1 = (int) (totalM1 * 0.2);
		    totalM2 = (int) (totalM2 * 0.2);

		    // Calcular el total general y total por cada uno
		    long recaudacionTotal = (long) (totalM1 + totalM2);
		    long recaudacionParcial = recaudacionTotal / 2;

		    // Crear y devolver la respuesta, usando la fecha del registro anterior
		    return new RecaudacionesResponse(
		        numeracionActual.getId(),
		        totalM1,
		        totalM2,
		        recaudacionTotal,
		        recaudacionParcial,
		        numeracionActual.getBar(),
		        numeracionAnterior.getFecha() // Usar la fecha del registro anterior
		    );
		}

}

