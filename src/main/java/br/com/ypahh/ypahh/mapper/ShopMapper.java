package br.com.ypahh.ypahh.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;

import br.com.ypahh.ypahh.datatransferobject.SimpleShopDTO;
import br.com.ypahh.ypahh.model.Shop;

public class ShopMapper {

	private static ModelMapper modelMapper = new ModelMapper();

	public static SimpleShopDTO mapper(Shop shop) {
		return modelMapper.map(shop, SimpleShopDTO.class);
	}

	public static Shop mapper(SimpleShopDTO simpleShopDTO) {
		Shop shop = modelMapper.map(simpleShopDTO, Shop.class);
		if (simpleShopDTO.getId() != null) {
			shop.setId(new ObjectId(simpleShopDTO.getId()));
		}
		return shop;
	}
	
	public static List<SimpleShopDTO> mapperListModelToDTO(List<Shop> shops) {
		return shops.stream().map(s -> mapper(s)).collect(Collectors.toList());
	}

	public static List<Shop> mapperListDTOTOModel(List<SimpleShopDTO> simpleShopDTOs) {
		return simpleShopDTOs.stream().map(s -> mapper(s)).collect(Collectors.toList());
	}

}
