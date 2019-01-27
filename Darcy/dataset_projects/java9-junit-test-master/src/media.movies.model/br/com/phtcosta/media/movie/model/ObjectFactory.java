//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2016.07.24 às 11:35:27 AM BRT 
//


package br.com.phtcosta.media.movie.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.com.phtcosta.media.movie.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.com.phtcosta.media.movie.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Movie }
     * 
     */
    public Movie createMovie() {
        return new Movie();
    }

    /**
     * Create an instance of {@link Movie.Fileinfo }
     * 
     */
    public Movie.Fileinfo createMovieFileinfo() {
        return new Movie.Fileinfo();
    }

    /**
     * Create an instance of {@link Movie.Fileinfo.Streamdetails }
     * 
     */
    public Movie.Fileinfo.Streamdetails createMovieFileinfoStreamdetails() {
        return new Movie.Fileinfo.Streamdetails();
    }

    /**
     * Create an instance of {@link Movie.Ids }
     * 
     */
    public Movie.Ids createMovieIds() {
        return new Movie.Ids();
    }

    /**
     * Create an instance of {@link Movie.Actor }
     * 
     */
    public Actor createMovieActor() {
        return new Actor();
    }

    /**
     * Create an instance of {@link Movie.Producer }
     * 
     */
    public Producer createMovieProducer() {
        return new Producer();
    }

    /**
     * Create an instance of {@link Movie.Fileinfo.Streamdetails.Video }
     * 
     */
    public Movie.Fileinfo.Streamdetails.Video createMovieFileinfoStreamdetailsVideo() {
        return new Movie.Fileinfo.Streamdetails.Video();
    }

    /**
     * Create an instance of {@link Movie.Fileinfo.Streamdetails.Audio }
     * 
     */
    public Movie.Fileinfo.Streamdetails.Audio createMovieFileinfoStreamdetailsAudio() {
        return new Movie.Fileinfo.Streamdetails.Audio();
    }

    /**
     * Create an instance of {@link Movie.Fileinfo.Streamdetails.Subtitle }
     * 
     */
    public Movie.Fileinfo.Streamdetails.Subtitle createMovieFileinfoStreamdetailsSubtitle() {
        return new Movie.Fileinfo.Streamdetails.Subtitle();
    }

    /**
     * Create an instance of {@link Movie.Ids.Entry }
     * 
     */
    public Movie.Ids.Entry createMovieIdsEntry() {
        return new Movie.Ids.Entry();
    }

}
