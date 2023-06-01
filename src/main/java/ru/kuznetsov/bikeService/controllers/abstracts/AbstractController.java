package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;

@Component
public abstract class AbstractController {
    public final static Logger logger = LoggerFactory.getLogger("BikeServiceLogger");
    protected UserService userService;
    protected PictureService pictureService;
    protected PDFService pdfService;
    public static String PDF_DOC_PATH;


    @Autowired
    public void setFontPath(@Value("${pdf.path}") String pdfDocPath) {
        PDF_DOC_PATH = pdfDocPath;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Autowired
    public void setPdfService(PDFService pdfService) {
        this.pdfService = pdfService;
    }
}
