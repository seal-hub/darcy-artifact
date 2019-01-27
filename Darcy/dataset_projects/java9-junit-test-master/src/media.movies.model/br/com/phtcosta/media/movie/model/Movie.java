//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2016.07.24 às 11:35:27 AM BRT 
//


package br.com.phtcosta.media.movie.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="originaltitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="set" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sorttitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rating" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="top250" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="votes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="outline" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="plot" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tagline" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="runtime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="thumb" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fanart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mpaa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="certification" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ids">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tmdbId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="trailer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="premiered" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="fileinfo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="streamdetails">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="video">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="aspect" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                                       &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                       &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                       &lt;element name="durationinseconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="audio" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="channels" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="subtitle">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="watched" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="playcount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="genre" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="studio" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="credits" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="director" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actor" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="role" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="thumb" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="producer" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="role" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="languages" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "originaltitle",
    "set",
    "sorttitle",
    "rating",
    "year",
    "top250",
    "votes",
    "outline",
    "plot",
    "tagline",
    "runtime",
    "thumb",
    "fanart",
    "mpaa",
    "certification",
    "id",
    "ids",
    "tmdbId",
    "trailer",
    "country",
    "premiered",
    "fileinfo",
    "watched",
    "playcount",
    "genre",
    "studio",
    "credits",
    "director",
    "actor",
    "producer",
    "languages"
})
@XmlRootElement(name = "movie")
public class Movie {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String originaltitle;
    @XmlElement(required = true)
    protected String set;
    @XmlElement(required = true)
    protected String sorttitle;
    protected double rating;
    protected int year;
    protected int top250;
    protected int votes;
    @XmlElement(required = true)
    protected String outline;
    @XmlElement(required = true)
    protected String plot;
    @XmlElement(required = true)
    protected String tagline;
    protected int runtime;
    @XmlElement(required = true)
    protected String thumb;
    @XmlElement(required = true)
    protected String fanart;
    @XmlElement(required = true)
    protected String mpaa;
    @XmlElement(required = true)
    protected String certification;
    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected Movie.Ids ids;
    protected int tmdbId;
    @XmlElement(required = true)
    protected String trailer;
    @XmlElement(required = true)
    protected String country;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar premiered;
    @XmlElement(required = true)
    protected Movie.Fileinfo fileinfo;
    protected boolean watched;
    protected int playcount;
    @XmlElement(required = true)
    protected List<String> genre;
    @XmlElement(required = true)
    protected List<String> studio;
    @XmlElement(required = true)
    protected List<String> credits;
    @XmlElement(required = true)
    protected String director;
    @XmlElement(required = true)
    protected List<Actor> actor;
    @XmlElement(required = true)
    protected List<Producer> producer;
    @XmlElement(required = true)
    protected String languages;

    /**
     * Obtém o valor da propriedade title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Define o valor da propriedade title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Obtém o valor da propriedade originaltitle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginaltitle() {
        return originaltitle;
    }

    /**
     * Define o valor da propriedade originaltitle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginaltitle(String value) {
        this.originaltitle = value;
    }

    /**
     * Obtém o valor da propriedade set.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSet() {
        return set;
    }

    /**
     * Define o valor da propriedade set.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSet(String value) {
        this.set = value;
    }

    /**
     * Obtém o valor da propriedade sorttitle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSorttitle() {
        return sorttitle;
    }

    /**
     * Define o valor da propriedade sorttitle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSorttitle(String value) {
        this.sorttitle = value;
    }

    /**
     * Obtém o valor da propriedade rating.
     * 
     */
    public double getRating() {
        return rating;
    }

    /**
     * Define o valor da propriedade rating.
     * 
     */
    public void setRating(double value) {
        this.rating = value;
    }

    /**
     * Obtém o valor da propriedade year.
     * 
     */
    public int getYear() {
        return year;
    }

    /**
     * Define o valor da propriedade year.
     * 
     */
    public void setYear(int value) {
        this.year = value;
    }

    /**
     * Obtém o valor da propriedade top250.
     * 
     */
    public int getTop250() {
        return top250;
    }

    /**
     * Define o valor da propriedade top250.
     * 
     */
    public void setTop250(int value) {
        this.top250 = value;
    }

    /**
     * Obtém o valor da propriedade votes.
     * 
     */
    public int getVotes() {
        return votes;
    }

    /**
     * Define o valor da propriedade votes.
     * 
     */
    public void setVotes(int value) {
        this.votes = value;
    }

    /**
     * Obtém o valor da propriedade outline.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutline() {
        return outline;
    }

    /**
     * Define o valor da propriedade outline.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutline(String value) {
        this.outline = value;
    }

    /**
     * Obtém o valor da propriedade plot.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Define o valor da propriedade plot.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlot(String value) {
        this.plot = value;
    }

    /**
     * Obtém o valor da propriedade tagline.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * Define o valor da propriedade tagline.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTagline(String value) {
        this.tagline = value;
    }

    /**
     * Obtém o valor da propriedade runtime.
     * 
     */
    public int getRuntime() {
        return runtime;
    }

    /**
     * Define o valor da propriedade runtime.
     * 
     */
    public void setRuntime(int value) {
        this.runtime = value;
    }

    /**
     * Obtém o valor da propriedade thumb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * Define o valor da propriedade thumb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThumb(String value) {
        this.thumb = value;
    }

    /**
     * Obtém o valor da propriedade fanart.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFanart() {
        return fanart;
    }

    /**
     * Define o valor da propriedade fanart.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFanart(String value) {
        this.fanart = value;
    }

    /**
     * Obtém o valor da propriedade mpaa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMpaa() {
        return mpaa;
    }

    /**
     * Define o valor da propriedade mpaa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMpaa(String value) {
        this.mpaa = value;
    }

    /**
     * Obtém o valor da propriedade certification.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertification() {
        return certification;
    }

    /**
     * Define o valor da propriedade certification.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertification(String value) {
        this.certification = value;
    }

    /**
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade ids.
     * 
     * @return
     *     possible object is
     *     {@link Movie.Ids }
     *     
     */
    public Movie.Ids getIds() {
        return ids;
    }

    /**
     * Define o valor da propriedade ids.
     * 
     * @param value
     *     allowed object is
     *     {@link Movie.Ids }
     *     
     */
    public void setIds(Movie.Ids value) {
        this.ids = value;
    }

    /**
     * Obtém o valor da propriedade tmdbId.
     * 
     */
    public int getTmdbId() {
        return tmdbId;
    }

    /**
     * Define o valor da propriedade tmdbId.
     * 
     */
    public void setTmdbId(int value) {
        this.tmdbId = value;
    }

    /**
     * Obtém o valor da propriedade trailer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrailer() {
        return trailer;
    }

    /**
     * Define o valor da propriedade trailer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrailer(String value) {
        this.trailer = value;
    }

    /**
     * Obtém o valor da propriedade country.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Define o valor da propriedade country.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Obtém o valor da propriedade premiered.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPremiered() {
        return premiered;
    }

    /**
     * Define o valor da propriedade premiered.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPremiered(XMLGregorianCalendar value) {
        this.premiered = value;
    }

    /**
     * Obtém o valor da propriedade fileinfo.
     * 
     * @return
     *     possible object is
     *     {@link Movie.Fileinfo }
     *     
     */
    public Movie.Fileinfo getFileinfo() {
        return fileinfo;
    }

    /**
     * Define o valor da propriedade fileinfo.
     * 
     * @param value
     *     allowed object is
     *     {@link Movie.Fileinfo }
     *     
     */
    public void setFileinfo(Movie.Fileinfo value) {
        this.fileinfo = value;
    }

    /**
     * Obtém o valor da propriedade watched.
     * 
     */
    public boolean isWatched() {
        return watched;
    }

    /**
     * Define o valor da propriedade watched.
     * 
     */
    public void setWatched(boolean value) {
        this.watched = value;
    }

    /**
     * Obtém o valor da propriedade playcount.
     * 
     */
    public int getPlaycount() {
        return playcount;
    }

    /**
     * Define o valor da propriedade playcount.
     * 
     */
    public void setPlaycount(int value) {
        this.playcount = value;
    }

    /**
     * Gets the value of the genre property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genre property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenre().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGenre() {
        if (genre == null) {
            genre = new ArrayList<String>();
        }
        return this.genre;
    }

    /**
     * Gets the value of the studio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStudio() {
        if (studio == null) {
            studio = new ArrayList<String>();
        }
        return this.studio;
    }

    /**
     * Gets the value of the credits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the credits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCredits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCredits() {
        if (credits == null) {
            credits = new ArrayList<String>();
        }
        return this.credits;
    }

    /**
     * Obtém o valor da propriedade director.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirector() {
        return director;
    }

    /**
     * Define o valor da propriedade director.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirector(String value) {
        this.director = value;
    }

    /**
     * Gets the value of the actor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Actor }
     * 
     * 
     */
    public List<Actor> getActor() {
        if (actor == null) {
            actor = new ArrayList<Actor>();
        }
        return this.actor;
    }

    /**
     * Gets the value of the producer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the producer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProducer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Producer }
     * 
     * 
     */
    public List<Producer> getProducer() {
        if (producer == null) {
            producer = new ArrayList<Producer>();
        }
        return this.producer;
    }

    /**
     * Obtém o valor da propriedade languages.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguages() {
        return languages;
    }

    /**
     * Define o valor da propriedade languages.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguages(String value) {
        this.languages = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="streamdetails">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="video">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="aspect" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                             &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                             &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                             &lt;element name="durationinseconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="audio" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="channels" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="subtitle">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "streamdetails"
    })
    public static class Fileinfo {

        @XmlElement(required = true)
        protected Movie.Fileinfo.Streamdetails streamdetails;

        /**
         * Obtém o valor da propriedade streamdetails.
         * 
         * @return
         *     possible object is
         *     {@link Movie.Fileinfo.Streamdetails }
         *     
         */
        public Movie.Fileinfo.Streamdetails getStreamdetails() {
            return streamdetails;
        }

        /**
         * Define o valor da propriedade streamdetails.
         * 
         * @param value
         *     allowed object is
         *     {@link Movie.Fileinfo.Streamdetails }
         *     
         */
        public void setStreamdetails(Movie.Fileinfo.Streamdetails value) {
            this.streamdetails = value;
        }


        /**
         * <p>Classe Java de anonymous complex type.
         * 
         * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="video">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="aspect" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *                   &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                   &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                   &lt;element name="durationinseconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="audio" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="channels" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="subtitle">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "video",
            "audio",
            "subtitle"
        })
        public static class Streamdetails {

            @XmlElement(required = true)
            protected Movie.Fileinfo.Streamdetails.Video video;
            @XmlElement(required = true)
            protected List<Movie.Fileinfo.Streamdetails.Audio> audio;
            @XmlElement(required = true)
            protected Movie.Fileinfo.Streamdetails.Subtitle subtitle;

            /**
             * Obtém o valor da propriedade video.
             * 
             * @return
             *     possible object is
             *     {@link Movie.Fileinfo.Streamdetails.Video }
             *     
             */
            public Movie.Fileinfo.Streamdetails.Video getVideo() {
                return video;
            }

            /**
             * Define o valor da propriedade video.
             * 
             * @param value
             *     allowed object is
             *     {@link Movie.Fileinfo.Streamdetails.Video }
             *     
             */
            public void setVideo(Movie.Fileinfo.Streamdetails.Video value) {
                this.video = value;
            }

            /**
             * Gets the value of the audio property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the audio property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAudio().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Movie.Fileinfo.Streamdetails.Audio }
             * 
             * 
             */
            public List<Movie.Fileinfo.Streamdetails.Audio> getAudio() {
                if (audio == null) {
                    audio = new ArrayList<Movie.Fileinfo.Streamdetails.Audio>();
                }
                return this.audio;
            }

            /**
             * Obtém o valor da propriedade subtitle.
             * 
             * @return
             *     possible object is
             *     {@link Movie.Fileinfo.Streamdetails.Subtitle }
             *     
             */
            public Movie.Fileinfo.Streamdetails.Subtitle getSubtitle() {
                return subtitle;
            }

            /**
             * Define o valor da propriedade subtitle.
             * 
             * @param value
             *     allowed object is
             *     {@link Movie.Fileinfo.Streamdetails.Subtitle }
             *     
             */
            public void setSubtitle(Movie.Fileinfo.Streamdetails.Subtitle value) {
                this.subtitle = value;
            }


            /**
             * <p>Classe Java de anonymous complex type.
             * 
             * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="channels" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "codec",
                "language",
                "channels"
            })
            public static class Audio {

                @XmlElement(required = true)
                protected String codec;
                @XmlElement(required = true)
                protected String language;
                protected int channels;

                /**
                 * Obtém o valor da propriedade codec.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodec() {
                    return codec;
                }

                /**
                 * Define o valor da propriedade codec.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodec(String value) {
                    this.codec = value;
                }

                /**
                 * Obtém o valor da propriedade language.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLanguage() {
                    return language;
                }

                /**
                 * Define o valor da propriedade language.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLanguage(String value) {
                    this.language = value;
                }

                /**
                 * Obtém o valor da propriedade channels.
                 * 
                 */
                public int getChannels() {
                    return channels;
                }

                /**
                 * Define o valor da propriedade channels.
                 * 
                 */
                public void setChannels(int value) {
                    this.channels = value;
                }

            }


            /**
             * <p>Classe Java de anonymous complex type.
             * 
             * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "language"
            })
            public static class Subtitle {

                @XmlElement(required = true)
                protected Object language;

                /**
                 * Obtém o valor da propriedade language.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Object }
                 *     
                 */
                public Object getLanguage() {
                    return language;
                }

                /**
                 * Define o valor da propriedade language.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Object }
                 *     
                 */
                public void setLanguage(Object value) {
                    this.language = value;
                }

            }


            /**
             * <p>Classe Java de anonymous complex type.
             * 
             * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="codec" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="aspect" type="{http://www.w3.org/2001/XMLSchema}double"/>
             *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *         &lt;element name="durationinseconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "codec",
                "aspect",
                "width",
                "height",
                "durationinseconds"
            })
            public static class Video {

                @XmlElement(required = true)
                protected String codec;
                protected double aspect;
                protected int width;
                protected int height;
                protected int durationinseconds;

                /**
                 * Obtém o valor da propriedade codec.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodec() {
                    return codec;
                }

                /**
                 * Define o valor da propriedade codec.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodec(String value) {
                    this.codec = value;
                }

                /**
                 * Obtém o valor da propriedade aspect.
                 * 
                 */
                public double getAspect() {
                    return aspect;
                }

                /**
                 * Define o valor da propriedade aspect.
                 * 
                 */
                public void setAspect(double value) {
                    this.aspect = value;
                }

                /**
                 * Obtém o valor da propriedade width.
                 * 
                 */
                public int getWidth() {
                    return width;
                }

                /**
                 * Define o valor da propriedade width.
                 * 
                 */
                public void setWidth(int value) {
                    this.width = value;
                }

                /**
                 * Obtém o valor da propriedade height.
                 * 
                 */
                public int getHeight() {
                    return height;
                }

                /**
                 * Define o valor da propriedade height.
                 * 
                 */
                public void setHeight(int value) {
                    this.height = value;
                }

                /**
                 * Obtém o valor da propriedade durationinseconds.
                 * 
                 */
                public int getDurationinseconds() {
                    return durationinseconds;
                }

                /**
                 * Define o valor da propriedade durationinseconds.
                 * 
                 */
                public void setDurationinseconds(int value) {
                    this.durationinseconds = value;
                }

            }

        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class Ids {

        @XmlElement(required = true)
        protected List<Movie.Ids.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Movie.Ids.Entry }
         * 
         * 
         */
        public List<Movie.Ids.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<Movie.Ids.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Classe Java de anonymous complex type.
         * 
         * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            @XmlElement(required = true)
            protected String key;
            @XmlElement(required = true)
            protected String value;

            /**
             * Obtém o valor da propriedade key.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Define o valor da propriedade key.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Obtém o valor da propriedade value.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Define o valor da propriedade value.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }    

}
