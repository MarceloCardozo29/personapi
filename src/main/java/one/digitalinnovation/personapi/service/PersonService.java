package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapperCustom;
import one.digitalinnovation.personapi.repository.PersonRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class PersonService {

    private PersonRepositoryCustom personRepositoryCustom;

    private static final PersonMapperCustom PERSON_MAPPER_CUSTOM = PersonMapperCustom.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        var personToSave = PERSON_MAPPER_CUSTOM.toModel(personDTO);

        var savedPerson = personRepositoryCustom.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Created person with ID ");
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepositoryCustom.findAll();
        return allPeople.stream()
                .map(PERSON_MAPPER_CUSTOM::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        var person = verifyIfExists(id);

        return PERSON_MAPPER_CUSTOM.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        verifyIfExists(id);
        personRepositoryCustom.deleteById(id);
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExists(id);

        var personToUpdate = PERSON_MAPPER_CUSTOM.toModel(personDTO);

        var updatedPerson = personRepositoryCustom.save(personToUpdate);
        return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepositoryCustom.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
