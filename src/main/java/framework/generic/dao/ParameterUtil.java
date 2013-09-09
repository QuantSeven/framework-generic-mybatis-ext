package framework.generic.dao;

import java.util.ArrayList;
import java.util.List;

public class ParameterUtil {
	public static Parameters parse(Object[] paramArrayOfObject) {
		Object localObject = null;
		Page localPage = null;
		ArrayList localArrayList = null;
		if (paramArrayOfObject != null)
			for (int i = 0; i < paramArrayOfObject.length; i++)
				if ((localObject == null) && ((paramArrayOfObject[i] instanceof List))) {
					List localList;
					if (((localList = (List) paramArrayOfObject[i]) != null) && (localList.size() > 0) && ((localList.get(0) instanceof Order)))
						localObject = (List) paramArrayOfObject[i];
				} else if ((localObject == null) && ((paramArrayOfObject[i] instanceof Order))) {
					((ArrayList) (localObject = new ArrayList())).add((Order) paramArrayOfObject[i]);
				} else if ((localPage == null) && ((paramArrayOfObject[i] instanceof Page))) {
					localPage = (Page) paramArrayOfObject[i];
				} else {
					if (localArrayList == null)
						localArrayList = new ArrayList();
					localArrayList.add(paramArrayOfObject[i]);
				}
		Parameters localParameters = new Parameters();
		if (localObject != null)
			localParameters.put("orders", localObject);
		if (localPage != null)
			localParameters.put("page", localPage);
		if (localArrayList != null)
			localParameters.put("args", localArrayList);
		return (Parameters) localParameters;
	}

	public static List<?> getArgs(Parameters paramParameters) {
		return (List) paramParameters.get("args");
	}

	public static boolean isModelArg(Parameters paramParameters) {
		return ((paramParameters = (Parameters) getArgs(paramParameters)) != null) && (paramParameters.size() == 1) && ((paramParameters.get(0) instanceof Model));
	}

	public static Model<?> getFirstArg(Parameters paramParameters) {
		if ((paramParameters = (Parameters) getArgs(paramParameters)) != null)
			return (Model) paramParameters.get(0);
		return null;
	}

	public static Page<?> getPage(Parameters paramParameters) {
		return (Page) paramParameters.get("page");
	}

	public static List<Order> getOrders(Parameters paramParameters) {
		return (List) paramParameters.get("orders");
	}
}