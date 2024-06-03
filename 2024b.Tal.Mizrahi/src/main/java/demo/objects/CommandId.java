package demo.objects;

public class CommandId {
		
		private String superApp;
		private String miniApp;
		private String id;
		
		public CommandId () {} 
		
		

		public CommandId(String superAppName, String miniApp, String id) {
			this.superApp = superAppName;
			this.miniApp = miniApp;
			this.id = id;
		}



		public String getSuperApp() {
			return superApp;
		}  

		public void setSuperApp(String superApp) {
			this.superApp = superApp;
		} 

		public String getMiniApp() {
			return miniApp;
		}

		public void setMiniApp(String miniApp) {
			this.miniApp = miniApp;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return "CommandId [superAppName=" + superApp 
					+ ", miniApp=" + miniApp 
					+ ", id=" + id + "]";
		}

} 
