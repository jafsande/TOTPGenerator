package controllers;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.CredentialRepository;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.defaultpages.error;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 */
@Singleton
public class QRController extends Controller {

	/**
	 * Formulario para introducir la cuenta de usuario para generar QR Code
	 */
    private final Form<UserData> userForm;

	/**
	 * Formulario para introducir el código de validación generado y la cuenta de usuario para autorización
	 */
    private final Form<CodeData> codeForm;
    
    private final GoogleAuthenticatorConfigBuilder gacb;
    private final CredentialRepository credRepo;
    private final GoogleAuthenticator ga;

    private MessagesApi messagesApi;
    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public QRController(FormFactory formFactory, MessagesApi messagesApi) {
        this.userForm = formFactory.form(UserData.class);
        this.codeForm = formFactory.form(CodeData.class);
        this.messagesApi = messagesApi;
        
        /* Creamos el repositorio de credenciales (es un HashMap) */
        this.credRepo = new CredentialRepository();

        /* Creamos la instancia de GAuthenticator */        
        this.gacb = new GoogleAuthenticatorConfigBuilder();
//        .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(120)).setWindowSize(30).setCodeDigits(6);
        this.ga = new GoogleAuthenticator(gacb.build());
        
        /* Asociadmos el repositorio de credenciales con la instancia de GAuthenticator */ 
        this.ga.setCredentialRepository(this.credRepo);	        
    }

    /**
     * Acción para leer un username introducido (email).
     * 
     * @param request
     * @return
     */
    public Result getUserName(Http.Request request) {
        return ok(views.html.getUserName.render(userForm, request, messagesApi.preferred(request)));
    }

    /**
     * Acción para crear un QRCode a partir del username introducido.
     * 
     * @param request
     * @return
     */
    public Result createQRCode(Http.Request request) {
        final Form<UserData> boundForm = userForm.bindFromRequest(request);
        
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.getUserName.render(boundForm, request, messagesApi.preferred(request)));
        
        } else {
            UserData data = boundForm.get();
            
            /* Creamos la key a partir del username introducido */
            final GoogleAuthenticatorKey key = ga.createCredentials(data.getUsername());
            
            /* Generamos la url para mostrar el QRCode */
            StringBuilder strUrl = new StringBuilder();
            strUrl.append("https://chart.googleapis.com/chart?chs=200x200&chld=M|0&cht=qr&chl=").append(GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("ACME", data.getUsername(), key));
            return ok(views.html.showQRCode.render(strUrl.toString(), request, messagesApi.preferred(request)));
        }
    }

    /**
     * Acción para leer un username y un código generado con GoogleAuthenticator. 
     * 
     * @param request
     * @return
     */
    public Result getCode(Http.Request request) {
        return ok(views.html.getCode.render(codeForm, request, messagesApi.preferred(request)));
    }
    
    /**
     * Acción para validar el par username-código introducido.
     * 
     * @param request
     * @return
     */
    public Result validateCode(Http.Request request) {
        final Form<CodeData> boundForm = codeForm.bindFromRequest(request);
        
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.getCode.render(boundForm, request, messagesApi.preferred(request)));
        
        } else {
            CodeData data = boundForm.get();
            
            /* Si la autorización es correcta, redirigimos a la página codeValidated */
            if (this.ga.authorizeUser(data.getUsername(), data.getCodeAsInt())) 
                        return ok(views.html.codeValidated.render(request, messagesApi.preferred(request)));
            /* Si la autorización no es correcta, redirigimos a la página codeNotValidated */
            else
                return ok(views.html.codeNotValidated.render(request, messagesApi.preferred(request)));
        }
    }

}
