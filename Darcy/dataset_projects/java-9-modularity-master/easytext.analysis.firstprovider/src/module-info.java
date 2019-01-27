import com.theara.easytext.service.Analysis;
import com.theara.modularity.easytext.service.impl.FlechKincaid;

module easytext.analysis {

    requires easytext.analysis.service;

    provides Analysis with FlechKincaid;

}