package br.com.ypahh.ypahh.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ConfigModelMapper {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
