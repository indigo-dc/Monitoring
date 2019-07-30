package com.deep.qcgprobe.beans;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 22/09/16.
 */
public class QCGResourcesBean {

	@SerializedName("date")
	@Expose
	private String date;
	@SerializedName("nodes")
	@Expose
	private Nodes nodes;
	@SerializedName("queues")
	@Expose
	private Queues queues;

	public QCGResourcesBean(String date, Nodes nodes, Queues queues) {
		this.date = date;
		this.nodes = nodes;
		this.queues = queues;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public QCGResourcesBean withDate(String date) {
		this.date = date;
		return this;
	}

	public Nodes getNodes() {
		return nodes;
	}

	public void setNodes(Nodes nodes) {
		this.nodes = nodes;
	}

	public QCGResourcesBean withNodes(Nodes nodes) {
		this.nodes = nodes;
		return this;
	}

	public Queues getQueues() {
		return queues;
	}

	public void setQueues(Queues queues) {
		this.queues = queues;
	}

	public QCGResourcesBean withQueues(Queues queues) {
		this.queues = queues;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("date", date).append("nodes", nodes).append("queues", queues)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(nodes).append(queues).append(date).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof QCGResourcesBean) == false) {
			return false;
		}
		QCGResourcesBean rhs = ((QCGResourcesBean) other);
		return new EqualsBuilder().append(nodes, rhs.nodes).append(queues, rhs.queues).append(date, rhs.date)
				.isEquals();
	}

	public class Total {

		@SerializedName("nodes")
		@Expose
		private long nodes;
		@SerializedName("cpus")
		@Expose
		private long cpus;
		@SerializedName("memory")
		@Expose
		private long memory;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public Total() {
		}

		/**
		 *
		 * @param cpus
		 * @param nodes
		 * @param memory
		 */
		public Total(long nodes, long cpus, long memory) {
			super();
			this.nodes = nodes;
			this.cpus = cpus;
			this.memory = memory;
		}

		public long getNodes() {
			return nodes;
		}

		public void setNodes(long nodes) {
			this.nodes = nodes;
		}

		public Total withNodes(long nodes) {
			this.nodes = nodes;
			return this;
		}

		public long getCpus() {
			return cpus;
		}

		public void setCpus(long cpus) {
			this.cpus = cpus;
		}

		public Total withCpus(long cpus) {
			this.cpus = cpus;
			return this;
		}

		public long getMemory() {
			return memory;
		}

		public void setMemory(long memory) {
			this.memory = memory;
		}

		public Total withMemory(long memory) {
			this.memory = memory;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("nodes", nodes).append("cpus", cpus).append("memory", memory)
					.toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(cpus).append(nodes).append(memory).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof Total) == false) {
				return false;
			}
			Total rhs = ((Total) other);
			return new EqualsBuilder().append(cpus, rhs.cpus).append(nodes, rhs.nodes).append(memory, rhs.memory)
					.isEquals();
		}

	}

	public class NodeStats {

		@SerializedName("total")
		@Expose
		private Total total;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public NodeStats() {
		}

		/**
		 *
		 * @param total
		 * @param states
		 */
		public NodeStats(Total total) {
			super();
			this.total = total;
		}

		public Total getTotal() {
			return total;
		}

		public void setTotal(Total total) {
			this.total = total;
		}

		public NodeStats withTotal(Total total) {
			this.total = total;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("total", total).toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(total).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof NodeStats) == false) {
				return false;
			}
			NodeStats rhs = ((NodeStats) other);
			return new EqualsBuilder().append(total, rhs.total).isEquals();
		}

	}

	public class Nodes {

		@SerializedName("node_stats")
		@Expose
		private NodeStats nodeStats;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public Nodes() {
		}

		/**
		 *
		 * @param nodeStats
		 * @param list
		 */
		public Nodes(NodeStats nodeStats) {
			super();
			this.nodeStats = nodeStats;
		}

		public NodeStats getNodeStats() {
			return nodeStats;
		}

		public void setNodeStats(NodeStats nodeStats) {
			this.nodeStats = nodeStats;
		}

		public Nodes withNodeStats(NodeStats nodeStats) {
			this.nodeStats = nodeStats;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("nodeStats", nodeStats).toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(nodeStats).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof Nodes) == false) {
				return false;
			}
			Nodes rhs = ((Nodes) other);
			return new EqualsBuilder().append(nodeStats, rhs.nodeStats).isEquals();
		}

	}

	public class Queues {

		@SerializedName("job_stats")
		@Expose
		private JobStats jobStats;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public Queues() {
		}

		/**
		 *
		 * @param list
		 * @param jobStats
		 */
		public Queues(JobStats jobStats) {
			super();

			this.jobStats = jobStats;
		}

		public JobStats getJobStats() {
			return jobStats;
		}

		public void setJobStats(JobStats jobStats) {
			this.jobStats = jobStats;
		}

		public Queues withJobStats(JobStats jobStats) {
			this.jobStats = jobStats;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("jobStats", jobStats).toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(jobStats).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof Queues) == false) {
				return false;
			}
			Queues rhs = ((Queues) other);
			return new EqualsBuilder().append(jobStats, rhs.jobStats).isEquals();
		}

	}

	public class Total_ {

		@SerializedName("jobs")
		@Expose
		private long jobs;
		@SerializedName("cpus")
		@Expose
		private long cpus;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public Total_() {
		}

		/**
		 *
		 * @param cpus
		 * @param jobs
		 */
		public Total_(long jobs, long cpus) {
			super();
			this.jobs = jobs;
			this.cpus = cpus;
		}

		public long getJobs() {
			return jobs;
		}

		public void setJobs(long jobs) {
			this.jobs = jobs;
		}

		public Total_ withJobs(long jobs) {
			this.jobs = jobs;
			return this;
		}

		public long getCpus() {
			return cpus;
		}

		public void setCpus(long cpus) {
			this.cpus = cpus;
		}

		public Total_ withCpus(long cpus) {
			this.cpus = cpus;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("jobs", jobs).append("cpus", cpus).toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(cpus).append(jobs).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof Total_) == false) {
				return false;
			}
			Total_ rhs = ((Total_) other);
			return new EqualsBuilder().append(cpus, rhs.cpus).append(jobs, rhs.jobs).isEquals();
		}

	}

	public class JobStats {

		@SerializedName("total")
		@Expose
		private Total_ total;

		/**
		 * No args constructor for use in serialization
		 *
		 */
		public JobStats() {
		}

		/**
		 *
		 * @param total
		 * @param states
		 */
		public JobStats(Total_ total) {
			super();
			this.total = total;
		}

		public Total_ getTotal() {
			return total;
		}

		public void setTotal(Total_ total) {
			this.total = total;
		}

		public JobStats withTotal(Total_ total) {
			this.total = total;
			return this;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("total", total).toString();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(total).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof JobStats) == false) {
				return false;
			}
			JobStats rhs = ((JobStats) other);
			return new EqualsBuilder().append(total, rhs.total).isEquals();
		}

	}

	public QCGResourcesBean() {		
	}

}
