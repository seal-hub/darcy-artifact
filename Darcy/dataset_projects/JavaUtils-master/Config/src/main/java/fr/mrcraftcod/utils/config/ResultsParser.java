package fr.mrcraftcod.utils.config;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
/**
 * Created by Tom on 16/08/2016.
 */
public class ResultsParser<T>
{
	private final Function<ResultSet, List<T>> parser;
	private final List<Consumer<List<T>>> parsedCallbacks;

	public ResultsParser(Function<ResultSet, List<T>> parser)
	{
		this.parsedCallbacks = new ArrayList<>();
		this.parser = parser;
	}

	public ResultsParser<T> parsed(Consumer<List<T>> callback)
	{
		this.parsedCallbacks.add(callback);
		return this;
	}

	public List<T> parse(ResultSet resultSet)
	{
		List<T> parsed = parser.apply(resultSet);
		parsedCallbacks.forEach(callback -> callback.accept(parsed));
		return parsed;
	}
}
