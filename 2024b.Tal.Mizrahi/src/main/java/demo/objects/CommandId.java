package demo.objects;

public class CommandId {
		
		private String superAppName;
		private String miniApp;
		private String id;
		
		public CommandId () {} 
		
		

		public CommandId(String superAppName, String miniApp, String id) {
			this.superAppName = superAppName;
			this.miniApp = miniApp;
			this.id = id;
		}



		public String getSuperAppName() {
			return superAppName;
		}  

		public void setSuperAppName(String superAppName) {
			this.superAppName = superAppName;
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
			return "CommandId [superAppName=" + superAppName 
					+ ", miniApp=" + miniApp 
					+ ", id=" + id + "]";
		}

} 
