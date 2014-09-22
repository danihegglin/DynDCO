package ch.uzh.dyndco.algorithms.dco.complete.dpop

class DPOP {
  
  // Create Vertexes and Edges in a pseudotree way (TreeEdge, BackEdge)
  
  

}

//  /** The UTIL propagation module */
//	protected UTILpropagation<V, U> utilModule;
//
//	/** The VALUE propagation module */
//	protected VALUEpropagation<V> valueModule;
//
//	/** The DFSgeneration module */
//	protected DFSgeneration<V, U> dfsModule;
//	
//	/** ****************** ESTABLISH GATHERERS ********************* **/
//	@Override
//	public ArrayList<StatsReporter> getSolGatherers() {
//
//		ArrayList<StatsReporter> solGatherers = new ArrayList<StatsReporter> (3);
//		
//		utilModule = new UTILpropagation<V, U>(null, problem);
//		utilModule.setSilent(true);
//		solGatherers.add(utilModule);
//		
//		valueModule = new VALUEpropagation<V> (null, problem);
//		valueModule.setSilent(true);
//		solGatherers.add(valueModule);
//		
//		Element params = new Element ("module");
//		params.setAttribute("DOTrenderer", DOTrenderer.class.getName());
//		dfsModule = new DFSgeneration<V, U> (params, problem);
//		dfsModule.setSilent(true);
//		solGatherers.add(dfsModule);
//		
//		return solGatherers;
//	}
//	
//	/** ****************** ESTABLISH GATHERERS ********************* **/
//  
//  // Utility Establishment
//  U optUtil = utilModule.getOptUtil();
//		if (optUtil == null) 
//			optUtil = super.problem.getZeroUtility();
//	
//	// Find Value
//	Map<String, V>  solution = valueModule.getSolution();
//		
//		// Neighbor Signals
//		int nbrMsgs = factory.getNbrMsgs();
//		TreeMap<String, Integer> msgNbrs = factory.getMsgNbrs();
//		long totalMsgSize = factory.getTotalMsgSize();
//		TreeMap<String, Long> msgSizes = factory.getMsgSizes();
//		long maxMsgSize = factory.getOverallMaxMsgSize();
//		TreeMap<String, Long> maxMsgSizes = factory.getMaxMsgSizes();
//		
//		// ???
//		long ncccs = factory.getNcccs();
//		int maxMsgDim = utilModule.getMaxMsgDim();
//		
//		// ???
//		int numberOfCoordinationConstraints = problem.getNumberOfCoordinationConstraints();
//		int nbrVariables = problem.getNbrVars();
		
		// Probably unusable
//		HashMap<String, Long> timesNeeded = new HashMap<String, Long> ();
//		timesNeeded.put(dfsModule.getClass().getName(), dfsModule.getFinalTime());
//		timesNeeded.put(utilModule.getClass().getName(), utilModule.getFinalTime());
//		timesNeeded.put(valueModule.getClass().getName(), valueModule.getFinalTime());
//		long totalTime = factory.getTime();

