import { AllCommunityModule, ModuleRegistry, enableDevValidations } from 'ag-grid-community'

ModuleRegistry.registerModules([AllCommunityModule])

if (import.meta.env.DEV) {
  enableDevValidations()
}
