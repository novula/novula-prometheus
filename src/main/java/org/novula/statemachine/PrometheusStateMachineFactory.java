package org.novula.statemachine;

public class PrometheusStateMachineFactory implements StateMachineFactory<Integer>
{
	@Override
	public StateMachine<Integer> create()
	{
		return new StateMachine<>(new State<>("init"));
	}
}
