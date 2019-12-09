package org.occideas.reporthistory.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
//import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.client.methods.RequestBuilder;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.occideas.qsf.response.QSFSurveyResponse;
import org.occideas.reporthistory.service.ReportHistoryService;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Path("/reportHistory")
public class ReportHistoryRestController {

    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private ReportHistoryService service;

    @Path(value = "/downloadReport")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces({"application/zip"})
    public Response downloadReport(ReportHistoryVO vo) throws IOException {

        String pathStr = makeZip(vo);

        java.nio.file.Path path = Paths.get(pathStr);
        return Response.ok(getOut(Files.readAllBytes(path), pathStr),
                javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = " + vo.getName())
                .build();
    }

    private String makeZip(ReportHistoryVO vo) {
        byte[] buffer = new byte[1024];

        String pathStr = vo.getPath() + ".zip";
        try {

            FileOutputStream fos = new FileOutputStream(pathStr);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(vo.getName());
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(vo.getPath());

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();

            zos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pathStr;
    }

    private StreamingOutput getOut(final byte[] excelBytes, String pathStr) {
        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
                try {
                    java.nio.file.Path path = Paths.get(pathStr);
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                } catch (Exception e) {
                    throw new WebApplicationException();
                }
            }
        };
        return fileStream;
    }

    @GET
    @Path(value = "/getAll")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getAll() {
        List<ReportHistoryVO> list = new ArrayList<>();
        try {
            list = service.getAll();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path(value = "/getByType")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getByType(@QueryParam("type") String type) {
        List<ReportHistoryVO> list = new ArrayList<>();
        try {
            list = service.getByType(type);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }

    @Path(value = "/save")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response save(ReportHistoryVO vo) {
        try {
            service.save(vo);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @Path(value = "/delete")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response delete(ReportHistoryVO vo) {
        try {
            service.delete(vo);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @Path(value = "/downloadLookup")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces({"application/zip"})
    public Response downloadLookup(ReportHistoryVO vo) throws IOException {

        String pathStr = vo.getPath().substring(0, vo.getPath().length() - 4).concat("-Lookup.csv");
        java.nio.file.Path path = Paths.get(pathStr);
        if (path.toFile().exists()) {
            vo.setPath(pathStr);
            String zipPathStr = makeZip(vo);
            return Response.ok(getOut(Files.readAllBytes(Paths.get(zipPathStr)), zipPathStr),
                    javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + vo.getName())
                    .build();
        }

        return Response.status(Status.NO_CONTENT).type("text/plain").build();
    }

    @Path(value = "/uploadQSF")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    public Response uploadQSF(ReportHistoryVO vo) throws IOException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", "TwWDAeQCPgsGmXdmSPdlwF1zvUu5txoSzzHGLRmV");
        headers.add("Content-type", "multipart/form-data");
        ClientConfig clientConfig = new ClientConfig();
        ObjectMapper JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_MAPPER.findAndRegisterModules();
        clientConfig.register(new JacksonJsonProvider(JSON_MAPPER));

        File file = new File(vo.getPath());
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File(vo.getPath()));
        filePart.type(new javax.ws.rs.core.MediaType("application","vnd.qualtrics.survey.qsf"));
        MultiPart multiPart = new FormDataMultiPart()
                .field("name", vo.getName().contains("_")?vo.getName().substring(0,
                        vo.getName().indexOf("_")):vo.getName())
                .bodyPart(filePart);

        QSFSurveyResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .register(MultiPartFeature.class)
                .build()
                .target("https://au1.qualtrics.com")
                .path("API/v3/surveys")
                .request()
                .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(multiPart,multiPart.getMediaType()))
                .readEntity(QSFSurveyResponse.class);

        if ("200 - OK".equals(response.getMeta().getHttpStatus())) {
            log.info(response);
            return Response.ok().build();
        } else {
            log.error(response.getMeta().getHttpStatus()+"->error:"+response.getMeta().getError().getErrorMessage());
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(response.getMeta().getHttpStatus()
                    +"->error:"+response.getMeta().getError().getErrorMessage()).build();
        }
    }

}
