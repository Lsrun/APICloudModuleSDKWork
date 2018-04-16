package common.model;

public class AppMemoryInfo {

	private long maxmemory;// Runtime.getRuntime().maxMemory();// 100663296

	private long nativeHeapSize;// Debug.getNativeHeapSize();// 本地堆大小
	private long nativeHeapAllocatedSize;// Debug.getNativeHeapAllocatedSize();//
											// 本地堆分配大小

	private long nativeHeapFreeSize;// Debug.getNativeHeapFreeSize();// 本地堆空闲大小

	private long availMem;// MemoryInfo info.availMem >> 10;// 系统剩余内存//1905608

	private boolean lowMemory;// MemoryInfo info.lowMemory;// 系统是否处于低内存运行

	private long threshold;// MemoryInfo info.threshold;//
							// 当系统剩余内存低于threshold值时就看成低内存运行

	public long getMaxmemory() {
		return maxmemory;
	}

	public void setMaxmemory(long maxmemory) {
		this.maxmemory = maxmemory;
	}

	public long getNativeHeapSize() {
		return nativeHeapSize;
	}

	public void setNativeHeapSize(long nativeHeapSize) {
		this.nativeHeapSize = nativeHeapSize;
	}

	public long getNativeHeapAllocatedSize() {
		return nativeHeapAllocatedSize;
	}

	public void setNativeHeapAllocatedSize(long nativeHeapAllocatedSize) {
		this.nativeHeapAllocatedSize = nativeHeapAllocatedSize;
	}

	public long getNativeHeapFreeSize() {
		return nativeHeapFreeSize;
	}

	public void setNativeHeapFreeSize(long nativeHeapFreeSize) {
		this.nativeHeapFreeSize = nativeHeapFreeSize;
	}

	public long getAvailMem() {
		return availMem;
	}

	public void setAvailMem(long availMem) {
		this.availMem = availMem;
	}

	public boolean isLowMemory() {
		return lowMemory;
	}

	public void setLowMemory(boolean lowMemory) {
		this.lowMemory = lowMemory;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

}