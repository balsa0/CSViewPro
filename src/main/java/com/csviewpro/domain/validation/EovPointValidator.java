package com.csviewpro.domain.validation;

import com.csviewpro.domain.model.GeoPoint;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Balsa on 2016. 10. 31..
 */
@Deprecated
public class EovPointValidator implements Validator{

	public static final double EOE_XY_BOUNDARY = 400000.00;
	public static final double EOV_Y_MAX_VALUE = 950000.00;

	@Override
	public boolean supports(Class<?> aClass) {
		return GeoPoint.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Double eovX = ((GeoPoint) o).getxCoo();
		Double eovY = ((GeoPoint) o).getyCoo();

		// validate X

		if(eovX == null)
			errors.rejectValue("xCoo", "eov.xCoo.isNull");

		if(eovX.isInfinite() || eovX.isNaN())
			errors.rejectValue("xCoo", "eov.xCoo.isNan");

		if(eovX < 0.00)
			errors.rejectValue("xCoo", "eov.xCoo.isNegative");

		if(eovX > EOE_XY_BOUNDARY)
			errors.rejectValue("xCoo", "eov.xCoo.tooBig");


		// validate Y

		if(eovY == null)
			errors.rejectValue("yCoo", "eov.yCoo.isNull");

		if(eovY.isInfinite() || eovY.isNaN())
			errors.rejectValue("yCoo", "eov.yCoo.isNan");

		if(eovY < EOE_XY_BOUNDARY)
			errors.rejectValue("yCoo", "eov.yCoo.tooSmall");

		if(eovY > EOV_Y_MAX_VALUE)
			errors.rejectValue("yCoo", "eov.yCoo.isTooBig");
	}
}
