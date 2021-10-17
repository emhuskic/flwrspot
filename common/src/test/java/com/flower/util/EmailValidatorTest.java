package com.flower.util;

import com.flower.util.email.EmailValidator;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintValidatorContext;
import static org.junit.Assert.assertEquals;

@ExtendWith(EasyMockExtension.class)
class EmailValidatorTest {

    @TestSubject
    private final EmailValidator emailValidator = new EmailValidator();

    @Mock
    private final ConstraintValidatorContext context = EasyMock.mock(ConstraintValidatorContext.class);

    @Test
    void test_isEmailValid() {
        EasyMock.replay(context);
        assertEquals(emailValidator.isValid("test@test.com", context), true);
        assertEquals(emailValidator.isValid("test.com", context), false);
        assertEquals(emailValidator.isValid("test", context), false);
        assertEquals(emailValidator.isValid("2@em@emina@test.com", context), false);
        EasyMock.verify(context);
    }

}
