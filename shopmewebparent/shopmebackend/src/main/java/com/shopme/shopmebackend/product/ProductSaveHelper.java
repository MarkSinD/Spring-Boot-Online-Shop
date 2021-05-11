package com.shopme.shopmebackend.product;

import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import com.shopme.shopmebackend.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class ProductSaveHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

    static void deleteExtraImagesWereRemovedOnForm(Product product) throws IOException {
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        Path pathDir = Paths.get(extraImageDir);


        try{
            Files.list(pathDir).forEach(file->{
                String filename = file.toFile().getName();

                if(!product.contrainsImageName(filename)){
                    try{
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory: " + pathDir);
        }
    }

    static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if(imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for(int count = 0; count < imageIDs.length; count++){
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if(detailNames == null || detailNames.length == 0) return;

        for(int count = 0; count < detailNames.length; count++){
            String name = detailNames[count];
            String value = detailValues[count];
            Integer id = Integer.parseInt(detailIDs[count]);

            // Для обновления
            if(id != 0){
                product.addProductDetail(id, name, value);
            }
            // Для создания новой детализации
            else if(!name.isEmpty() && !value.isEmpty()){
                product.addProductDetail(name, value);
            }
        }
    }

    // Сохранение фотографий в директорию
    static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            String uploadDir = "product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if(extraImageMultiparts.length > 0) {
            String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : extraImageMultiparts) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }

    }

    // Сохранение фотографий в обьект
    static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if(extraImageMultiparts.length > 0){
            for(MultipartFile multipartFile : extraImageMultiparts){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                    if(!product.contrainsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }

    }
    // Сохранение фотографий в обьект
    static void setMainImageName(MultipartFile mainImageMultipart, Product product                        ){
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }
}
